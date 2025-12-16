package com.example.libreria_api.service.gestionpedidos;

import com.example.libreria_api.dto.gestionpedidos.*;
import com.example.libreria_api.exception.ResourceNotFoundException;
import com.example.libreria_api.model.experienciausuarios.ContactoFormulario;
import com.example.libreria_api.model.gestionpedidos.EstadoPedido;
import com.example.libreria_api.model.gestionpedidos.HistorialEstadoPedido;
import com.example.libreria_api.model.gestionpedidos.Pedido;
import com.example.libreria_api.model.gestionpedidos.Render3d;
import com.example.libreria_api.model.personalizacionproductos.Personalizacion;
import com.example.libreria_api.model.sistemausuarios.SesionAnonima;
import com.example.libreria_api.model.sistemausuarios.Usuario;
import com.example.libreria_api.repository.experienciausuarios.ContactoFormularioRepository;
import com.example.libreria_api.repository.gestionpedidos.EstadoPedidoRepository;
import com.example.libreria_api.repository.gestionpedidos.HistorialEstadoPedidoRepository;
import com.example.libreria_api.repository.gestionpedidos.PedidoRepository;
import com.example.libreria_api.repository.gestionpedidos.Render3dRepository;
import com.example.libreria_api.repository.personalizacionproductos.PersonalizacionRepository;
import com.example.libreria_api.repository.sistemausuarios.UsuarioRepository;
import com.example.libreria_api.repository.sistemausuarios.SesionAnonimaRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private static final String UPLOAD_DIR = "uploads/renders/";

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstadoPedidoRepository estadoPedidoRepository;
    private final PersonalizacionRepository personalizacionRepository;
    private final ContactoFormularioRepository contactoRepository;
    private final Render3dRepository render3dRepository;
    private final HistorialEstadoPedidoRepository historialRepository;
    private final SesionAnonimaRepository sesionAnonimaRepository;


    public PedidoService(PedidoRepository pedidoRepository,
                         UsuarioRepository usuarioRepository,
                         EstadoPedidoRepository estadoPedidoRepository,
                         PersonalizacionRepository personalizacionRepository,
                         ContactoFormularioRepository contactoRepository,
                         Render3dRepository render3dRepository,
                         HistorialEstadoPedidoRepository historialRepository,
                         SesionAnonimaRepository sesionAnonimaRepository) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.estadoPedidoRepository = estadoPedidoRepository;
        this.personalizacionRepository = personalizacionRepository;
        this.contactoRepository = contactoRepository;
        this.render3dRepository = render3dRepository;
        this.historialRepository = historialRepository;
        this.sesionAnonimaRepository = sesionAnonimaRepository;

        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al inicializar el directorio de carga.", e);
        }
    }

    private String guardarArchivo(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null) {
            int dotIndex = originalFilename.lastIndexOf('.');
            if (dotIndex > 0) {
                extension = originalFilename.substring(dotIndex);
            }
        }

        String newFilename = UUID.randomUUID().toString() + extension;
        Path filePath = Paths.get(UPLOAD_DIR, newFilename);

        Files.copy(file.getInputStream(), filePath);

        return UPLOAD_DIR + newFilename;
    }


    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> obtenerTodosLosPedidos() {
        System.out.println(">>> INICIANDO CONSULTA DE PEDIDOS...");
        List<Pedido> pedidos = pedidoRepository.findAllWithEstadoEagerly();

        if (pedidos.isEmpty()) {
            return List.of();
        }

        return pedidos.stream().map(pedido -> {
            PedidoResponseDTO dto = PedidoMapper.toPedidoResponseDTO(pedido);


            if (pedido.getCliente() != null) {
                dto.setNombreCliente(pedido.getCliente().getUsuNombre());
            } else if (pedido.getPedIdentificadorCliente() != null) {
                dto.setNombreCliente(pedido.getPedIdentificadorCliente());
            } else {
                dto.setNombreCliente("Anónimo / Desconocido");
            }


            if (pedido.getEmpleadoAsignado() != null) {
                dto.setNombreEmpleado(pedido.getEmpleadoAsignado().getUsuNombre());
            } else {
                dto.setNombreEmpleado("PENDIENTE ASIGNAR");
            }


            try {
                render3dRepository.findTopRenderByPedId(pedido.getPed_id())
                        .ifPresent(render -> {
                            dto.setRenderPath(render.getRenImagen());
                        });
            } catch (Exception e) {
                System.err.println(">>> Advertencia: Error buscando Render para Pedido ID " + pedido.getPed_id());
            }

            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)

    public PedidoResponseDTO obtenerPedidoPorId(Integer id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", "id", id));


        PedidoResponseDTO dto = PedidoMapper.toPedidoResponseDTO(pedido);


        dto = enriquecerDTOConNombres(pedido, dto);

        return dto;
    }

    @Transactional
    public PedidoResponseDTO guardarPedido(PedidoRequestDTO requestDTO, MultipartFile render) {
        try {

            Pedido nuevoPedido = new Pedido();
            nuevoPedido.setPedComentarios(requestDTO.getPedComentarios());

            // 1. LÓGICA DE ESTADO
            final Integer finalIdEstado = (requestDTO.getEstId() != null) ? requestDTO.getEstId() : 1;
            EstadoPedido estado = estadoPedidoRepository.findById(finalIdEstado)
                    .orElseThrow(() -> new ResourceNotFoundException("EstadoPedido", "id", finalIdEstado));
            nuevoPedido.setEstadoPedido(estado);


            String codigoGenerado = generarCodigoPedido();
            nuevoPedido.setPedCodigo(codigoGenerado);
            nuevoPedido.setPedFechaCreacion(new Date());


            if (requestDTO.getPerId() != null) {
                Personalizacion personalizacion = personalizacionRepository.findById(requestDTO.getPerId())
                        .orElseThrow(() -> new ResourceNotFoundException("Personalizacion", "id", requestDTO.getPerId()));
                nuevoPedido.setPersonalizacion(personalizacion);
            }


            if (requestDTO.getUsuIdEmpleado() != null) {
                Usuario empleado = usuarioRepository.findById(requestDTO.getUsuIdEmpleado())
                        .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", requestDTO.getUsuIdEmpleado()));
                nuevoPedido.setEmpleadoAsignado(empleado);
            }

            if (requestDTO.getSesionId() != null) {
                SesionAnonima sesion = sesionAnonimaRepository.findById(requestDTO.getSesionId())
                        .orElseThrow(() -> new ResourceNotFoundException("SesionAnonima", "id", requestDTO.getSesionId()));
                nuevoPedido.setSesion(sesion);
            }


            if (requestDTO.getUsuIdCliente() != null) {
                Usuario cliente = usuarioRepository.findById(requestDTO.getUsuIdCliente())
                        .orElseThrow(() -> new ResourceNotFoundException("Usuario Cliente", "id", requestDTO.getUsuIdCliente()));
                nuevoPedido.setCliente(cliente);
            }


            nuevoPedido.setPedIdentificadorCliente(requestDTO.getPedIdentificadorCliente());


            Pedido pedidoGuardado = pedidoRepository.saveAndFlush(nuevoPedido);



            return PedidoMapper.toPedidoResponseDTO(pedidoGuardado);

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar pedido: " + e.getMessage(), e);
        }
    }

    @Transactional
    public PedidoResponseDTO actualizar(Integer id, PedidoRequestDTO requestDTO, MultipartFile render) {
        // El método map de Optional requiere que la lambda devuelva un valor.
        return pedidoRepository.findById(id).map(pedidoExistente -> {

            PedidoResponseDTO responseDTO;

            try {

                EstadoPedido estadoActualizado = null;
                EstadoPedido nuevoEstado = null;

                if (requestDTO.getEstId() != null) {
                    final Integer nuevoEstadoId = requestDTO.getEstId();


                    nuevoEstado = estadoPedidoRepository.findById(nuevoEstadoId)
                            .orElseThrow(() -> new ResourceNotFoundException("EstadoPedido", "id", nuevoEstadoId));

                    pedidoExistente.setEstadoPedido(nuevoEstado);
                    estadoActualizado = nuevoEstado;
                }

                if (requestDTO.getPerId() != null) {
                    Personalizacion personalizacion = personalizacionRepository.findById(requestDTO.getPerId())
                            .orElseThrow(() -> new ResourceNotFoundException("Personalizacion", "id", requestDTO.getPerId()));
                    pedidoExistente.setPersonalizacion(personalizacion);
                }
                if (requestDTO.getUsuIdEmpleado() != null) {
                    Usuario empleado = usuarioRepository.findById(requestDTO.getUsuIdEmpleado())
                            .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", requestDTO.getUsuIdEmpleado()));
                    pedidoExistente.setEmpleadoAsignado(empleado);
                }

                if (requestDTO.getUsuIdCliente() != null) {
                    Usuario cliente = usuarioRepository.findById(requestDTO.getUsuIdCliente())
                            .orElseThrow(() -> new ResourceNotFoundException("Usuario Cliente", "id", requestDTO.getUsuIdCliente()));
                    pedidoExistente.setCliente(cliente);
                }

                if (requestDTO.getPedComentarios() != null) {
                    pedidoExistente.setPedComentarios(requestDTO.getPedComentarios());
                }
                if (requestDTO.getPedIdentificadorCliente() != null) {
                    pedidoExistente.setPedIdentificadorCliente(requestDTO.getPedIdentificadorCliente());
                }


                if (render != null && !render.isEmpty()) {
                    String rutaArchivo = guardarArchivo(render);

                }


                Pedido pedidoActualizado = pedidoRepository.saveAndFlush(pedidoExistente);


                responseDTO = PedidoMapper.toPedidoResponseDTO(pedidoActualizado);


                if (estadoActualizado != null) {
                    responseDTO.setEstId(estadoActualizado.getEst_id());
                    responseDTO.setEstadoNombre(estadoActualizado.getEstNombre());
                }


                return responseDTO;

            } catch (IOException e) {

                throw new RuntimeException("Error I/O al guardar el archivo: " + e.getMessage(), e);
            } catch (ResourceNotFoundException e) {

                throw new RuntimeException(e.getMessage(), e);
            } catch (Exception e) {
                System.err.println("ERROR CRÍTICO al actualizar pedido ID " + id + ": " + e.getMessage());
                throw new RuntimeException("Error al actualizar pedido: " + e.getMessage(), e);
            }
        }).orElse(null);
    }
    @Transactional
    public boolean eliminarPedido(Integer id) {
        if (pedidoRepository.existsById(id)) {
            pedidoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public long contarPedidosPorEstado(String nombreEstado) {
        return pedidoRepository.countByEstadoPedido_EstNombre(nombreEstado);
    }

    @Transactional(readOnly = true)
    public long contarPedidosPorEstadoId(Integer estadoId) {
        return pedidoRepository.countByEstadoId(estadoId);
    }

    @Transactional(readOnly = true)
    public long contarTotalPedidos() {
        return pedidoRepository.count();
    }

    @Transactional
    public PedidoResponseDTO crearDesdeContacto(
            Integer contactoId,
            Integer estadoId,
            String comentarios,
            Integer usuIdEmpleado
    ) {

        ContactoFormulario contacto = contactoRepository.findById(contactoId)
                .orElseThrow(() -> new EntityNotFoundException("Contacto no encontrado"));

        Pedido nuevoPedido = new Pedido();


        nuevoPedido.setConId(contactoId);


        if (contacto.getPersonalizacion() != null) {

            nuevoPedido.setPersonalizacion(contacto.getPersonalizacion());
        }


        if (contacto.getUsuario() != null) {

            nuevoPedido.setCliente(contacto.getUsuario());
        } else if (contacto.getSesion() != null) {

            nuevoPedido.setSesion(contacto.getSesion());


            nuevoPedido.setPedIdentificadorCliente(
                    contacto.getConNombre() + " - " +
                            (contacto.getConTelefono() != null ? contacto.getConTelefono() : contacto.getConCorreo())
            );
        } else {

            nuevoPedido.setPedIdentificadorCliente(contacto.getConNombre() + " - " + contacto.getConTelefono());
        }


        if (usuIdEmpleado != null) {
            Usuario empleado = usuarioRepository.findById(usuIdEmpleado)
                    .orElseThrow(() -> new EntityNotFoundException("Empleado asignado no encontrado"));
            nuevoPedido.setEmpleadoAsignado(empleado);
        } else {

            nuevoPedido.setEmpleadoAsignado(null);
        }


        nuevoPedido.setPedCodigo(generarCodigoPedido());
        nuevoPedido.setPedFechaCreacion(new Date());


        String comentarioDefault = String.format(
                "Pedido creado desde contacto de %s. Mensaje: %s",
                contacto.getConNombre(),
                contacto.getConMensaje().substring(0, Math.min(100, contacto.getConMensaje().length()))
        );
        nuevoPedido.setPedComentarios(comentarios != null && !comentarios.isEmpty() ? comentarios : comentarioDefault);

        // Estado
        EstadoPedido estado = estadoPedidoRepository.findById(estadoId != null ? estadoId : 1)
                .orElseThrow(() -> new EntityNotFoundException("Estado no encontrado"));
        nuevoPedido.setEstadoPedido(estado);


        Pedido guardado = pedidoRepository.save(nuevoPedido);


        Usuario responsable = usuarioRepository.findById(usuIdEmpleado != null ? usuIdEmpleado : 2) // Usamos el ID de quien crea
                .orElse(null);

        HistorialEstadoPedido historial = new HistorialEstadoPedido();
        historial.setPedido(guardado);
        historial.setEstadoPedido(estado);
        historial.setHisComentarios("Pedido creado. " + nuevoPedido.getPedComentarios());
        historial.setUsuarioResponsable(responsable);
        historialRepository.save(historial);


        System.out.println("Pedido creado desde contacto: " + guardado.getPedCodigo());

        return PedidoMapper.toPedidoResponseDTO(guardado);
    }

    // Método auxiliar para generar código único
    private String generarCodigoPedido() {
        String prefijo = "PED-";
        String timestamp = String.valueOf(System.currentTimeMillis());
        return prefijo + timestamp.substring(timestamp.length() - 8);
    }

    /**
     * Registra un cambio de estado en el historial y actualiza el estado actual del pedido.
     * @param pedidoId ID del pedido a actualizar.
     * @param nuevoEstadoId ID del nuevo estado.
     * @param comentarios Comentario del cambio (his_comentarios).
     * @param responsableId ID del usuario que realiza el cambio.
     * @return DTO del pedido actualizado.
     */
    @Transactional
    public PedidoResponseDTO actualizarEstadoConHistorial(
            Integer pedidoId,
            Integer nuevoEstadoId,
            String comentarios,
            Integer responsableId) {


        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", "id", pedidoId));

        EstadoPedido nuevoEstado = estadoPedidoRepository.findById(nuevoEstadoId)
                .orElseThrow(() -> new ResourceNotFoundException("EstadoPedido", "id", nuevoEstadoId));

        Usuario responsable = usuarioRepository.findById(responsableId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", responsableId));


        pedido.setEstadoPedido(nuevoEstado);
        Pedido pedidoActualizado = pedidoRepository.save(pedido); // Guarda el cambio de estado en la tabla principal


        HistorialEstadoPedido historial = new HistorialEstadoPedido();
        historial.setPedido(pedidoActualizado);
        historial.setEstadoPedido(nuevoEstado);
        historial.setHisComentarios(comentarios);
        historial.setUsuarioResponsable(responsable);

        historialRepository.save(historial);


        return PedidoMapper.toPedidoResponseDTO(pedidoActualizado);
    }

    /**
     * Obtiene el historial completo de estados de un pedido.
     * @param pedidoId ID del pedido.
     * @return Lista de HistorialResponseDTO ordenada por fecha descendente.
     */
    @Transactional(readOnly = true)
    public List<HistorialResponseDTO> obtenerHistorialPorPedido(Integer pedidoId) {

        return historialRepository.findHistorialConDetalles(pedidoId)
                .stream()
                .map(HistorialMapper::toResponseDTO)
                .collect(Collectors.toList());
    }


    @Transactional
    public PedidoResponseDTO asignarEmpleado(Integer pedidoId, Integer usuIdEmpleado, Integer responsableId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", "id", pedidoId));


        Usuario nuevoEmpleado = usuarioRepository.findById(usuIdEmpleado)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario Empleado", "id", usuIdEmpleado));


        Usuario responsable = usuarioRepository.findById(responsableId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario Responsable", "id", responsableId));


        pedido.setEmpleadoAsignado(nuevoEmpleado);
        String comentarios = "Asignado a diseñador: " + nuevoEmpleado.getUsuNombre();
        Pedido pedidoActualizado = pedidoRepository.save(pedido);


        HistorialEstadoPedido historial = new HistorialEstadoPedido();
        historial.setPedido(pedidoActualizado);
        historial.setEstadoPedido(pedidoActualizado.getEstadoPedido());
        historial.setHisComentarios(comentarios);
        historial.setUsuarioResponsable(responsable);



        historialRepository.save(historial);


        PedidoResponseDTO dto = PedidoMapper.toPedidoResponseDTO(pedidoActualizado);

        return enriquecerDTOConNombres(pedidoActualizado, dto);
    }

    /**
     * Método auxiliar para rellenar los campos nombreCliente y nombreEmpleado en el DTO.
     */
    private PedidoResponseDTO enriquecerDTOConNombres(Pedido pedido, PedidoResponseDTO dto) {

        if (pedido.getCliente() != null) {
            dto.setNombreCliente(pedido.getCliente().getUsuNombre());
        } else if (pedido.getPedIdentificadorCliente() != null) {
            dto.setNombreCliente(pedido.getPedIdentificadorCliente());
        } else {
            dto.setNombreCliente("Anónimo / Desconocido");
        }

        if (pedido.getEmpleadoAsignado() != null) {
            dto.setNombreEmpleado(pedido.getEmpleadoAsignado().getUsuNombre());
        } else {
            dto.setNombreEmpleado("PENDIENTE ASIGNAR");
        }



        return dto;
    }


    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> obtenerPedidosPorCliente(Integer usuIdCliente) {
        List<Pedido> pedidos = pedidoRepository.findByClienteUsuId(usuIdCliente);

        return pedidos.stream().map(pedido -> {
            PedidoResponseDTO dto = PedidoMapper.toPedidoResponseDTO(pedido);

            return enriquecerDTOConNombres(pedido, dto);
        }).collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> obtenerPedidosPorEmpleado(Integer usuIdEmpleado) {
        List<Pedido> pedidos = pedidoRepository.findByEmpleadoAsignadoUsuId(usuIdEmpleado);

        return pedidos.stream().map(pedido -> {
            PedidoResponseDTO dto = PedidoMapper.toPedidoResponseDTO(pedido);

            return enriquecerDTOConNombres(pedido, dto);
        }).collect(Collectors.toList());
    }





}
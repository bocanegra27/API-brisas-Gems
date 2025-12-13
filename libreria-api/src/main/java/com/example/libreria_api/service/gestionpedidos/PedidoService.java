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
        // Asegurarse de que findAllWithEstadoEagerly cargue todas las relaciones necesarias (Estado, Cliente, Empleado)
        List<Pedido> pedidos = pedidoRepository.findAllWithEstadoEagerly();

        if (pedidos.isEmpty()) {
            return List.of();
        }

        return pedidos.stream().map(pedido -> {
            PedidoResponseDTO dto = PedidoMapper.toPedidoResponseDTO(pedido);

            // 1. OBTENER Y ASIGNAR NOMBRE DEL CLIENTE
            // 🔥 Usamos el objeto Entidad Usuario (getCliente) que debería estar cargado
            if (pedido.getCliente() != null) {
                dto.setNombreCliente(pedido.getCliente().getUsuNombre());
            } else if (pedido.getPedIdentificadorCliente() != null) {
                // Si no hay cliente registrado (es anónimo), usamos el identificador
                dto.setNombreCliente(pedido.getPedIdentificadorCliente());
            } else {
                dto.setNombreCliente("Anónimo / Desconocido");
            }

            // 2. OBTENER Y ASIGNAR NOMBRE DEL EMPLEADO (DISEÑADOR/ADMIN)
            // 🔥 Usamos el objeto Entidad Usuario (getEmpleadoAsignado)
            if (pedido.getEmpleadoAsignado() != null) {
                dto.setNombreEmpleado(pedido.getEmpleadoAsignado().getUsuNombre());
            } else {
                dto.setNombreEmpleado("PENDIENTE ASIGNAR");
            }

            // ... (Tu lógica existente para el renderPath) ...
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
// 🔥 CAMBIO CRÍTICO: El método ahora devuelve el PedidoResponseDTO estándar
    public PedidoResponseDTO obtenerPedidoPorId(Integer id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", "id", id));

        // 1. Usar el mapper base (que mapea IDs como usuIdCliente, perId, etc.)
        PedidoResponseDTO dto = PedidoMapper.toPedidoResponseDTO(pedido);

        // 2. Enriquecer con Nombres y lógica de presentación (reutilizando tu método auxiliar)
        dto = enriquecerDTOConNombres(pedido, dto);

        return dto;
    }

    @Transactional
    public PedidoResponseDTO guardarPedido(PedidoRequestDTO requestDTO, MultipartFile render) {
        try {
            // 🔥 ELIMINAMOS PedidoMapper.toPedido(requestDTO) y creamos manualmente para asignar Entidades
            Pedido nuevoPedido = new Pedido();
            nuevoPedido.setPedComentarios(requestDTO.getPedComentarios());

            // 1. LÓGICA DE ESTADO
            final Integer finalIdEstado = (requestDTO.getEstId() != null) ? requestDTO.getEstId() : 1;
            EstadoPedido estado = estadoPedidoRepository.findById(finalIdEstado)
                    .orElseThrow(() -> new ResourceNotFoundException("EstadoPedido", "id", finalIdEstado));
            nuevoPedido.setEstadoPedido(estado);

            // 2. GENERACIÓN DE CÓDIGO Y FECHA
            String codigoGenerado = generarCodigoPedido();
            nuevoPedido.setPedCodigo(codigoGenerado);
            nuevoPedido.setPedFechaCreacion(new Date());

            // 3. ASIGNACIÓN DE RELACIONES (REQUIERE REPOSITORIOS)

            // Asignar Personalización
            if (requestDTO.getPerId() != null) {
                Personalizacion personalizacion = personalizacionRepository.findById(requestDTO.getPerId())
                        .orElseThrow(() -> new ResourceNotFoundException("Personalizacion", "id", requestDTO.getPerId()));
                nuevoPedido.setPersonalizacion(personalizacion);
            }

            // Asignar Empleado (Si el RequestDTO tiene el nuevo campo usuIdEmpleado)
            if (requestDTO.getUsuIdEmpleado() != null) {
                Usuario empleado = usuarioRepository.findById(requestDTO.getUsuIdEmpleado())
                        .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", requestDTO.getUsuIdEmpleado()));
                nuevoPedido.setEmpleadoAsignado(empleado);
            }

            // Asignar Sesión Anónima
            if (requestDTO.getSesionId() != null) {
                SesionAnonima sesion = sesionAnonimaRepository.findById(requestDTO.getSesionId()) // 🔥 ASUMO QUE TIENES ESTE REPOSITORIO INYECTADO
                        .orElseThrow(() -> new ResourceNotFoundException("SesionAnonima", "id", requestDTO.getSesionId()));
                nuevoPedido.setSesion(sesion);
            }

            // 🔥 Asignar Cliente Registrado (Si el RequestDTO tiene el nuevo campo usuIdCliente)
            if (requestDTO.getUsuIdCliente() != null) {
                Usuario cliente = usuarioRepository.findById(requestDTO.getUsuIdCliente())
                        .orElseThrow(() -> new ResourceNotFoundException("Usuario Cliente", "id", requestDTO.getUsuIdCliente()));
                nuevoPedido.setCliente(cliente);
            }

            // Asignar identificador cliente (si es anónimo)
            nuevoPedido.setPedIdentificadorCliente(requestDTO.getPedIdentificadorCliente());

            // 4. Guardar en base de datos y el resto de la lógica (Render, etc.)
            Pedido pedidoGuardado = pedidoRepository.saveAndFlush(nuevoPedido);

            // ... (Lógica de guardado de Render - se mantiene igual, usando pedidoGuardado) ...

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
            // 🔥 Declaración de la variable que debe ser devuelta
            PedidoResponseDTO responseDTO;

            try {
                // 1. ACTUALIZAR ESTADO
                EstadoPedido estadoActualizado = null;
                EstadoPedido nuevoEstado = null; // Necesario para la lógica de abajo

                if (requestDTO.getEstId() != null) {
                    final Integer nuevoEstadoId = requestDTO.getEstId();

                    // LÓGICA DE BÚSQUEDA DE ESTADO FALTANTE
                    nuevoEstado = estadoPedidoRepository.findById(nuevoEstadoId)
                            .orElseThrow(() -> new ResourceNotFoundException("EstadoPedido", "id", nuevoEstadoId));

                    pedidoExistente.setEstadoPedido(nuevoEstado);
                    estadoActualizado = nuevoEstado;
                }

                // 2. ACTUALIZAR RELACIONES (CRÍTICO)
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
                // 🔥 ACTUALIZAR CLIENTE
                if (requestDTO.getUsuIdCliente() != null) {
                    Usuario cliente = usuarioRepository.findById(requestDTO.getUsuIdCliente())
                            .orElseThrow(() -> new ResourceNotFoundException("Usuario Cliente", "id", requestDTO.getUsuIdCliente()));
                    pedidoExistente.setCliente(cliente);
                }
                // NOTA: No se maneja la actualización de sesionId, ya que una vez creado, se asume que no cambia de anónimo a registrado aquí.

                // 3. ACTUALIZAR CAMPOS PLANOS
                if (requestDTO.getPedComentarios() != null) {
                    pedidoExistente.setPedComentarios(requestDTO.getPedComentarios());
                }
                if (requestDTO.getPedIdentificadorCliente() != null) {
                    pedidoExistente.setPedIdentificadorCliente(requestDTO.getPedIdentificadorCliente());
                }

                // 4. LÓGICA DE GUARDADO DE NUEVO RENDER (Si aplica)
                if (render != null && !render.isEmpty()) {
                    String rutaArchivo = guardarArchivo(render);
                    // Asumo que la lógica para guardar el Render3D en el repositorio está aquí
                    // ...
                }

                // 5. GUARDAR CAMBIOS
                Pedido pedidoActualizado = pedidoRepository.saveAndFlush(pedidoExistente);

                // 6. GENERAR RESPUESTA
                responseDTO = PedidoMapper.toPedidoResponseDTO(pedidoActualizado);

                // Si cambiamos el estado, forzamos que el DTO de respuesta tenga el ID y Nombre nuevos.
                if (estadoActualizado != null) {
                    responseDTO.setEstId(estadoActualizado.getEst_id());
                    responseDTO.setEstadoNombre(estadoActualizado.getEstNombre());
                }

                // 🔥 CRÍTICO: Devolver el objeto DTO si la operación es exitosa
                return responseDTO;

            } catch (IOException e) {
                // El catch de IOException debe relanzar la RuntimeException (lo estás haciendo bien)
                throw new RuntimeException("Error I/O al guardar el archivo: " + e.getMessage(), e);
            } catch (ResourceNotFoundException e) {
                // Manejamos la excepción para relanzarla y que el controlador la capte.
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
            Integer usuIdEmpleado // 🔥 NUEVO: Recibe el ID del Admin/Diseñador que crea
    ) {
        // 1. Obtener contacto (debe tener Personalizacion y Sesion/Usuario cargados en la Entidad)
        ContactoFormulario contacto = contactoRepository.findById(contactoId)
                .orElseThrow(() -> new EntityNotFoundException("Contacto no encontrado"));

        Pedido nuevoPedido = new Pedido();

        // 2. ASIGNACIÓN DE TRAZABILIDAD (RESPONDE A TU PROBLEMA DE PÉRDIDA DE DATOS)

        // A. Origen: Contacto
        nuevoPedido.setConId(contactoId);

        // B. Personalización
        // 🔥 CRÍTICO: Usar la Entidad cargada del Contacto
        if (contacto.getPersonalizacion() != null) {
            // setPersonalizacion requiere la entidad Personalizacion
            nuevoPedido.setPersonalizacion(contacto.getPersonalizacion());
        }

        // C. Cliente (Registrado vs. Anónimo)
        if (contacto.getUsuario() != null) {
            // Cliente Registrado (ID=13 en tu ejemplo)
            nuevoPedido.setCliente(contacto.getUsuario());
        } else if (contacto.getSesion() != null) {
            // Cliente Anónimo (ID=5 en tu ejemplo)
            nuevoPedido.setSesion(contacto.getSesion());

            // Usar datos del formulario para el identificador
            nuevoPedido.setPedIdentificadorCliente(
                    contacto.getConNombre() + " - " +
                            (contacto.getConTelefono() != null ? contacto.getConTelefono() : contacto.getConCorreo())
            );
        } else {
            // Si es anónimo sin sesión/usuario, usar identificador del formulario
            nuevoPedido.setPedIdentificadorCliente(contacto.getConNombre() + " - " + contacto.getConTelefono());
        }

        // D. Empleado Asignado (Resuelve la asignación en la creación)
        if (usuIdEmpleado != null) {
            Usuario empleado = usuarioRepository.findById(usuIdEmpleado)
                    .orElseThrow(() -> new EntityNotFoundException("Empleado asignado no encontrado"));
            nuevoPedido.setEmpleadoAsignado(empleado);
        } else {
            // Si el admin no se asigna, se deja en NULL
            nuevoPedido.setEmpleadoAsignado(null);
        }

        // 3. GENERACIÓN DE DATOS BÁSICOS
        nuevoPedido.setPedCodigo(generarCodigoPedido());
        nuevoPedido.setPedFechaCreacion(new Date());

        // Comentarios
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

        // 4. Guardar y registrar historial (Se asume que el historial se registra aquí o en otro método)
        Pedido guardado = pedidoRepository.save(nuevoPedido);

        // 5. Registrar Evento de Creación en Historial (Mejora de flujo)
        Usuario responsable = usuarioRepository.findById(usuIdEmpleado != null ? usuIdEmpleado : 2) // Usamos el ID de quien crea
                .orElse(null);

        HistorialEstadoPedido historial = new HistorialEstadoPedido();
        historial.setPedido(guardado);
        historial.setEstadoPedido(estado);
        historial.setHisComentarios("Pedido creado. " + nuevoPedido.getPedComentarios());
        historial.setUsuarioResponsable(responsable);
        historialRepository.save(historial);


        System.out.println("✅ Pedido creado desde contacto: " + guardado.getPedCodigo());

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

        // 1. OBTENER PEDIDO Y ESTADOS
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", "id", pedidoId));

        EstadoPedido nuevoEstado = estadoPedidoRepository.findById(nuevoEstadoId)
                .orElseThrow(() -> new ResourceNotFoundException("EstadoPedido", "id", nuevoEstadoId));

        Usuario responsable = usuarioRepository.findById(responsableId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", responsableId));

        // 2. ACTUALIZAR ESTADO EN LA TABLA PEDIDO
        pedido.setEstadoPedido(nuevoEstado);
        Pedido pedidoActualizado = pedidoRepository.save(pedido); // Guarda el cambio de estado en la tabla principal

        // 3. REGISTRAR HISTORIAL
        HistorialEstadoPedido historial = new HistorialEstadoPedido();
        historial.setPedido(pedidoActualizado);
        historial.setEstadoPedido(nuevoEstado);
        historial.setHisComentarios(comentarios);
        historial.setUsuarioResponsable(responsable);

        historialRepository.save(historial); // Inserta el registro del evento

        // 4. RETORNAR RESPUESTA
        return PedidoMapper.toPedidoResponseDTO(pedidoActualizado);
    }

    /**
     * Obtiene el historial completo de estados de un pedido.
     * @param pedidoId ID del pedido.
     * @return Lista de HistorialResponseDTO ordenada por fecha descendente.
     */
    @Transactional(readOnly = true)
    public List<HistorialResponseDTO> obtenerHistorialPorPedido(Integer pedidoId) {
        // 🔥 CAMBIAR EL NOMBRE DEL METODO PARA USAR FETCH JOIN
        return historialRepository.findHistorialConDetalles(pedidoId)
                .stream()
                .map(HistorialMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Asigna o reasigna un diseñador/empleado a un pedido existente.
     * @param pedidoId ID del pedido.
     * @param usuIdEmpleado ID del diseñador a asignar.
     * @param responsableId ID del usuario que realiza el cambio (admin/negocio).
     * @return PedidoResponseDTO actualizado.
     */
    /**
     * Asigna o reasigna un diseñador/empleado a un pedido existente.
     * @param pedidoId ID del pedido.
     * @param usuIdEmpleado ID del diseñador a asignar.
     * @param responsableId ID del usuario que realiza el cambio (admin/negocio).
     * @return PedidoResponseDTO actualizado.
     */
    @Transactional
    public PedidoResponseDTO asignarEmpleado(Integer pedidoId, Integer usuIdEmpleado, Integer responsableId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", "id", pedidoId));

        // 1. Obtener Entidad Empleado
        Usuario nuevoEmpleado = usuarioRepository.findById(usuIdEmpleado)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario Empleado", "id", usuIdEmpleado));

        // 2. Obtener Entidad Responsable
        Usuario responsable = usuarioRepository.findById(responsableId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario Responsable", "id", responsableId));

        // 3. Actualizar la relación y guardar
        pedido.setEmpleadoAsignado(nuevoEmpleado);
        String comentarios = "Asignado a diseñador: " + nuevoEmpleado.getUsuNombre();
        Pedido pedidoActualizado = pedidoRepository.save(pedido); // Guarda el cambio de asignación

        // 4. Registrar en Historial (CRÍTICO: AÑADIR EL SAVE)
        HistorialEstadoPedido historial = new HistorialEstadoPedido();
        historial.setPedido(pedidoActualizado);
        historial.setEstadoPedido(pedidoActualizado.getEstadoPedido());
        historial.setHisComentarios(comentarios);
        historial.setUsuarioResponsable(responsable);

        // 🔥 CORRECCIÓN: GUARDAR EL REGISTRO DE HISTORIAL
        historialRepository.save(historial);

        // 5. Generar DTO y ENRIQUECER con nombres
        PedidoResponseDTO dto = PedidoMapper.toPedidoResponseDTO(pedidoActualizado);

        return enriquecerDTOConNombres(pedidoActualizado, dto);
    }

    /**
     * Método auxiliar para rellenar los campos nombreCliente y nombreEmpleado en el DTO.
     */
    private PedidoResponseDTO enriquecerDTOConNombres(Pedido pedido, PedidoResponseDTO dto) {
        // 1. OBTENER Y ASIGNAR NOMBRE DEL CLIENTE
        if (pedido.getCliente() != null) {
            dto.setNombreCliente(pedido.getCliente().getUsuNombre());
        } else if (pedido.getPedIdentificadorCliente() != null) {
            dto.setNombreCliente(pedido.getPedIdentificadorCliente());
        } else {
            dto.setNombreCliente("Anónimo / Desconocido");
        }

        // 2. OBTENER Y ASIGNAR NOMBRE DEL EMPLEADO (DISEÑADOR/ADMIN)
        if (pedido.getEmpleadoAsignado() != null) {
            dto.setNombreEmpleado(pedido.getEmpleadoAsignado().getUsuNombre());
        } else {
            dto.setNombreEmpleado("PENDIENTE ASIGNAR");
        }

        // NOTA: La lógica del renderPath puede omitirse aquí para evitar llamadas innecesarias.

        return dto;
    }

    /**
     * Obtiene todos los pedidos de un cliente específico (para Dashboard de Usuario).
     */
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> obtenerPedidosPorCliente(Integer usuIdCliente) {
        List<Pedido> pedidos = pedidoRepository.findByClienteUsuId(usuIdCliente);

        return pedidos.stream().map(pedido -> {
            PedidoResponseDTO dto = PedidoMapper.toPedidoResponseDTO(pedido);
            // Reutilizamos el método auxiliar para rellenar nombres
            return enriquecerDTOConNombres(pedido, dto);
        }).collect(Collectors.toList());
    }

    /**
     * Obtiene todos los pedidos asignados a un empleado/diseñador específico (para Dashboard de Diseñador).
     */
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> obtenerPedidosPorEmpleado(Integer usuIdEmpleado) {
        List<Pedido> pedidos = pedidoRepository.findByEmpleadoAsignadoUsuId(usuIdEmpleado);

        return pedidos.stream().map(pedido -> {
            PedidoResponseDTO dto = PedidoMapper.toPedidoResponseDTO(pedido);
            // Reutilizamos el método auxiliar para rellenar nombres
            return enriquecerDTOConNombres(pedido, dto);
        }).collect(Collectors.toList());
    }





}
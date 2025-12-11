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


    public PedidoService(PedidoRepository pedidoRepository,
                         UsuarioRepository usuarioRepository,
                         EstadoPedidoRepository estadoPedidoRepository,
                         PersonalizacionRepository personalizacionRepository,
                         ContactoFormularioRepository contactoRepository,
                         Render3dRepository render3dRepository,
                         HistorialEstadoPedidoRepository historialRepository) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.estadoPedidoRepository = estadoPedidoRepository;
        this.personalizacionRepository = personalizacionRepository;
        this.contactoRepository = contactoRepository;
        this.render3dRepository = render3dRepository;
        this.historialRepository = historialRepository;

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
        // Usamos el método que fuerza la carga del estado (EAGER FETCH)
        List<Pedido> pedidos = pedidoRepository.findAllWithEstadoEagerly();

        if (pedidos.isEmpty()) {
            return List.of();
        }

        return pedidos.stream().map(pedido -> {
            PedidoResponseDTO dto = PedidoMapper.toPedidoResponseDTO(pedido);

            // LÓGICA DE RENDER: Solo para la carga inicial de la lista
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
    public PedidoDetailResponseDTO obtenerPedidoPorId(Integer id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", "id", id));

        Personalizacion personalizacion = null;
        if (pedido.getPerId() != null) {
            personalizacion = personalizacionRepository.findById(pedido.getPerId()).orElse(null);
        }

        Usuario cliente = null;
        if (personalizacion != null && personalizacion.getUsuario() != null) {
            cliente = personalizacion.getUsuario();
        }

        Usuario empleado = null;
        if (pedido.getUsuIdEmpleado() != null) {
            empleado = usuarioRepository.findById(pedido.getUsuIdEmpleado()).orElse(null);
        }

        PedidoDetailResponseDTO dto = new PedidoDetailResponseDTO();
        dto.setPed_id(pedido.getPed_id());
        dto.setPedCodigo(pedido.getPedCodigo());
        dto.setPedFechaCreacion(pedido.getPedFechaCreacion());
        dto.setPedComentarios(pedido.getPedComentarios());

        if (pedido.getEstadoPedido() != null) {
            dto.setEstId(pedido.getEstadoPedido().getEst_id());
            dto.setEstadoNombre(pedido.getEstadoPedido().getEstNombre());
        } else {
            dto.setEstadoNombre("Desconocido");
        }

        dto.setClienteNombre(cliente != null ? cliente.getUsuNombre() : "Desconocido");
        dto.setEmpleadoNombre(empleado != null ? empleado.getUsuNombre() : "No asignado");

        return dto;
    }

    @Transactional
    public PedidoResponseDTO guardarPedido(PedidoRequestDTO requestDTO, MultipartFile render) {
        try {
            Pedido nuevoPedido = PedidoMapper.toPedido(requestDTO);

            // 1. LÓGICA DE ESTADO POR DEFECTO
            final Integer finalIdEstado = (requestDTO.getEstId() != null) ? requestDTO.getEstId() : 1;

            EstadoPedido estado = estadoPedidoRepository.findById(finalIdEstado)
                    .orElseThrow(() -> new ResourceNotFoundException("EstadoPedido", "id", finalIdEstado));

            nuevoPedido.setEstadoPedido(estado);

            // 2. LÓGICA DE GENERACIÓN DE CÓDIGO AUTOMÁTICO
            String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String randomSuffix = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
            String codigoGenerado = "PED-" + fecha + "-" + randomSuffix;

            nuevoPedido.setPedCodigo(codigoGenerado);

            // Mapear sesión anónima si existe
            if (requestDTO.getSesionId() != null) {
                SesionAnonima sesion = new SesionAnonima();
                sesion.setSesId(requestDTO.getSesionId());
                nuevoPedido.setSesion(sesion);
            }

            // 3. Guardar en base de datos
            Pedido pedidoGuardado = pedidoRepository.saveAndFlush(nuevoPedido);

            // 4. LÓGICA DE GUARDADO DE RENDER
            if (render != null && !render.isEmpty()) {
                String rutaArchivo = guardarArchivo(render);

                Render3d nuevoRender = new Render3d();
                nuevoRender.setPedido(pedidoGuardado);
                nuevoRender.setRenFechaAprobacion(LocalDate.now());
                nuevoRender.setRenImagen(rutaArchivo);

                render3dRepository.save(nuevoRender);
            }

            return PedidoMapper.toPedidoResponseDTO(pedidoGuardado);

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw new RuntimeException("Error I/O al guardar el archivo: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar pedido: " + e.getMessage(), e);
        }
    }

    @Transactional
    public PedidoResponseDTO actualizar(Integer id, PedidoRequestDTO requestDTO, MultipartFile render) {
        return pedidoRepository.findById(id).map(pedidoExistente -> {
            try {
                // Usar el nuevo método del mapper para actualizar campos básicos
                PedidoMapper.updatePedidoFromDTO(pedidoExistente, requestDTO);

                EstadoPedido estadoActualizado = null; // Variable para guardar el nuevo estado explícitamente

                // 1. ACTUALIZAR ESTADO
                if (requestDTO.getEstId() != null) {
                    final Integer nuevoEstadoId = requestDTO.getEstId();

                    EstadoPedido nuevoEstado = estadoPedidoRepository.findById(nuevoEstadoId)
                            .orElseThrow(() -> new ResourceNotFoundException("EstadoPedido", "id", nuevoEstadoId));

                    pedidoExistente.setEstadoPedido(nuevoEstado);
                    estadoActualizado = nuevoEstado; // Guardamos referencia
                }

                // 2. LÓGICA DE GUARDADO DE NUEVO RENDER
                if (render != null && !render.isEmpty()) {
                    String rutaArchivo = guardarArchivo(render);

                    Render3d nuevoRender = new Render3d();
                    nuevoRender.setRenImagen(rutaArchivo);
                    nuevoRender.setRenFechaAprobacion(LocalDate.now());
                    nuevoRender.setPedido(pedidoExistente);

                    render3dRepository.save(nuevoRender);
                }

                // 3. GUARDAR CAMBIOS (saveAndFlush fuerza el commit inmediato)
                Pedido pedidoActualizado = pedidoRepository.saveAndFlush(pedidoExistente);

                // 4. GENERAR RESPUESTA Y CORREGIR DATOS "STALE" (OBSOLETOS)
                PedidoResponseDTO responseDTO = PedidoMapper.toPedidoResponseDTO(pedidoActualizado);

                // Si cambiamos el estado, forzamos que el DTO de respuesta tenga el ID y Nombre nuevos.
                if (estadoActualizado != null) {
                    responseDTO.setEstId(estadoActualizado.getEst_id());
                    responseDTO.setEstadoNombre(estadoActualizado.getEstNombre());
                    // NO AGREGAMOS LÓGICA DEL RENDER PATH AQUÍ
                }

                return responseDTO;

            } catch (IOException e) {
                throw new RuntimeException("Error I/O al guardar el archivo: " + e.getMessage(), e);
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
    public PedidoResponseDTO crearDesdeContacto(Integer contactoId, Integer estadoId, String comentarios, Integer personalizacionId) {
        // Obtener contacto
        ContactoFormulario contacto = contactoRepository.findById(contactoId)
                .orElseThrow(() -> new EntityNotFoundException("Contacto no encontrado"));

        // Crear pedido
        Pedido pedido = new Pedido();

        // Generar código único
        String codigo = generarCodigoPedido();
        pedido.setPedCodigo(codigo);

        // Fechas
        pedido.setPedFechaCreacion(new Date());

        // Comentarios
        if (comentarios != null && !comentarios.isEmpty()) {
            pedido.setPedComentarios(comentarios);
        } else {
            // Comentario por defecto con info del contacto
            String comentarioAuto = String.format(
                    "Pedido creado desde contacto de %s (%s). Mensaje: %s",
                    contacto.getConNombre(),
                    contacto.getConCorreo(),
                    contacto.getConMensaje().substring(0, Math.min(100, contacto.getConMensaje().length()))
            );
            pedido.setPedComentarios(comentarioAuto);
        }

        // Estado
        if (estadoId != null) {
            EstadoPedido estado = estadoPedidoRepository.findById(estadoId)
                    .orElseThrow(() -> new EntityNotFoundException("Estado no encontrado"));
            pedido.setEstadoPedido(estado);
        } else {
            // Estado por defecto: 1 (Pendiente)
            EstadoPedido estadoDefault = estadoPedidoRepository.findById(1)
                    .orElseThrow(() -> new EntityNotFoundException("Estado default no encontrado"));
            pedido.setEstadoPedido(estadoDefault);
        }

        // VINCULAR CONTACTO
        pedido.setConId(contactoId);

        // VINCULAR PERSONALIZACIÓN (si existe)
        if (contacto.getPersonalizacion() != null) {
            pedido.setPerId(contacto.getPersonalizacion().getPerId());
        }

        // VINCULAR SESIÓN ANÓNIMA (si existe)
        if (contacto.getSesion() != null) {
            SesionAnonima sesion = new SesionAnonima();
            sesion.setSesId(contacto.getSesion().getSesId());
            pedido.setSesion(sesion);
        }

        // VINCULAR USUARIO (si existe)
        if (contacto.getUsuario() != null) {
            pedido.setUsuIdEmpleado(contacto.getUsuario().getUsuId());
        } else {
            // Si es anónimo o externo, usar identificador
            pedido.setPedIdentificadorCliente(
                    contacto.getConNombre() + " - " +
                            (contacto.getConTelefono() != null ? contacto.getConTelefono() : contacto.getConCorreo())
            );
        }

        // Guardar
        Pedido guardado = pedidoRepository.save(pedido);

        // Log
        System.out.println("✅ Pedido creado desde contacto: " + guardado.getPedCodigo());

        // Retornar DTO
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


}
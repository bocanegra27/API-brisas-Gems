package com.example.libreria_api.service.gestionpedidos;

import com.example.libreria_api.dto.gestionpedidos.*;
import com.example.libreria_api.exception.ResourceNotFoundException;
import com.example.libreria_api.model.gestionpedidos.EstadoPedido;
import com.example.libreria_api.model.gestionpedidos.HistorialEstadoPedido;
import com.example.libreria_api.model.gestionpedidos.Pedido;
import com.example.libreria_api.model.personalizacionproductos.Personalizacion;
import com.example.libreria_api.model.sistemausuarios.Usuario;
import com.example.libreria_api.repository.experienciausuarios.ContactoFormularioRepository;
import com.example.libreria_api.repository.gestionpedidos.EstadoPedidoRepository;
import com.example.libreria_api.repository.gestionpedidos.HistorialEstadoPedidoRepository;
import com.example.libreria_api.repository.gestionpedidos.PedidoRepository;
import com.example.libreria_api.repository.gestionpedidos.Render3dRepository;
import com.example.libreria_api.repository.personalizacionproductos.PersonalizacionRepository;
import com.example.libreria_api.repository.sistemausuarios.UsuarioRepository;
import com.example.libreria_api.repository.sistemausuarios.SesionAnonimaRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private static final String UPLOAD_DIR_RENDERS = "uploads/renders/";
    private static final String UPLOAD_DIR_HISTORIAL = "uploads/historial/";

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

        // Inicializar directorios
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR_RENDERS));
            Files.createDirectories(Paths.get(UPLOAD_DIR_HISTORIAL));
        } catch (IOException e) {
            throw new RuntimeException("Error al inicializar directorios de carga.", e);
        }
    }

    // Método genérico para guardar archivos
    private String guardarArchivoGenerico(MultipartFile file, String directorio) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        }
        String newFilename = UUID.randomUUID().toString() + extension;
        Path filePath = Paths.get(directorio, newFilename);
        Files.copy(file.getInputStream(), filePath);

        return "/" + directorio + newFilename;
    }

    // Método legacy para renders (si se usa en otro lado)
    private String guardarArchivo(MultipartFile file) throws IOException {
        String path = guardarArchivoGenerico(file, UPLOAD_DIR_RENDERS);
        return path.startsWith("/") ? path.substring(1) : path;
    }

    // ========================================================================
    // 🔥 MÉTODOS DEL USUARIO (MIS PEDIDOS)
    // ========================================================================

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> obtenerMisPedidos(String email) {
        // 1. Buscamos al usuario por su correo
        Usuario usuario = usuarioRepository.findByUsuCorreo(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", email));

        // 2. Usamos su ID para buscar los pedidos (Esto llama al repositorio corregido con LEFT JOIN)
        List<Pedido> pedidos = pedidoRepository.findByClienteUsuId(usuario.getUsuId());

        // 3. Convertimos a DTO
        return pedidos.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PedidoResponseDTO obtenerMiPedidoPorId(Integer id, String email) {
        Usuario usuario = usuarioRepository.findByUsuCorreo(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", email));

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", "id", id));

        // Validación de Seguridad: ¿El pedido le pertenece a este usuario?
        if (pedido.getCliente() == null || !pedido.getCliente().getUsuId().equals(usuario.getUsuId())) {
            throw new RuntimeException("ACCESO DENEGADO: No tienes permiso para ver este pedido.");
        }

        return enriquecerDTOConNombres(pedido, PedidoMapper.toPedidoResponseDTO(pedido));
    }

    @Transactional
    public PedidoResponseDTO crearMiPedido(PedidoRequestDTO requestDTO, MultipartFile render, String email) {
        Usuario usuario = usuarioRepository.findByUsuCorreo(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", email));

        Pedido pedido = new Pedido();
        pedido.setPedFechaCreacion(new Date());

        // 🔥 CORRECCIÓN: Usamos el getter correcto del DTO
        pedido.setPedComentarios(requestDTO.getPedComentarios());

        pedido.setPedCodigo("P-" + System.currentTimeMillis());

        // Asignamos al cliente logueado
        pedido.setCliente(usuario);

        // Estado inicial
        EstadoPedido estadoInicial = estadoPedidoRepository.findById(1) // 1 = Pendiente
                .orElseThrow(() -> new RuntimeException("Estado inicial no encontrado"));
        pedido.setEstadoPedido(estadoInicial);

        // Personalización (si aplica)
        if (requestDTO.getPerId() != null) {
            Personalizacion personalizacion = personalizacionRepository.findById(requestDTO.getPerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Personalizacion", "id", requestDTO.getPerId()));
            pedido.setPersonalizacion(personalizacion);
        }

        Pedido guardado = pedidoRepository.save(pedido);

        // Crear primer historial
        HistorialEstadoPedido historial = new HistorialEstadoPedido();
        historial.setPedido(guardado);
        historial.setEstadoPedido(estadoInicial);
        historial.setHisFechaCambio(new Date());
        historial.setHisComentarios("Pedido creado por el usuario.");
        historial.setUsuarioResponsable(usuario);
        historialRepository.save(historial);

        return PedidoMapper.toPedidoResponseDTO(guardado);
    }

    // ========================================================================
    // 🔥 MÉTODOS DE ADMINISTRACIÓN / GENERALES
    // ========================================================================

    @Transactional
    public PedidoResponseDTO actualizarEstadoConHistorial(Integer pedidoId, Integer nuevoEstadoId, String comentarios, Integer responsableId, MultipartFile imagen) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", "id", pedidoId));

        EstadoPedido nuevoEstado = estadoPedidoRepository.findById(nuevoEstadoId)
                .orElseThrow(() -> new ResourceNotFoundException("EstadoPedido", "id", nuevoEstadoId));

        Usuario responsable = usuarioRepository.findById(responsableId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", responsableId));

        pedido.setEstadoPedido(nuevoEstado);
        Pedido pedidoActualizado = pedidoRepository.save(pedido);

        HistorialEstadoPedido historial = new HistorialEstadoPedido();
        historial.setPedido(pedidoActualizado);
        historial.setEstadoPedido(nuevoEstado);
        historial.setHisFechaCambio(new Date());
        historial.setHisComentarios(comentarios);
        historial.setUsuarioResponsable(responsable);

        if (imagen != null && !imagen.isEmpty()) {
            try {
                String rutaImagen = guardarArchivoGenerico(imagen, UPLOAD_DIR_HISTORIAL);
                historial.setHisImagen(rutaImagen);
            } catch (IOException e) {
                throw new RuntimeException("Error al guardar imagen de historial", e);
            }
        }

        historialRepository.save(historial);
        return PedidoMapper.toPedidoResponseDTO(pedidoActualizado);
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> obtenerTodosLosPedidos() {
        return pedidoRepository.findAllWithEstadoEagerly().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PedidoResponseDTO obtenerPedidoPorId(Integer id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", "id", id));
        return enriquecerDTOConNombres(pedido, PedidoMapper.toPedidoResponseDTO(pedido));
    }

    // Auxiliar para no repetir código
    private PedidoResponseDTO convertirADTO(Pedido pedido) {
        return enriquecerDTOConNombres(pedido, PedidoMapper.toPedidoResponseDTO(pedido));
    }

    // Método auxiliar para enriquecer el DTO con nombres de cliente Y EMPLEADO
    private PedidoResponseDTO enriquecerDTOConNombres(Pedido pedido, PedidoResponseDTO dto) {
        // PRIORIDAD 1: Cliente Registrado
        if (pedido.getCliente() != null) {
            dto.setNombreCliente(pedido.getCliente().getUsuNombre());

            // PRIORIDAD 2: Cliente Manual/Anónimo
        } else if (pedido.getPedIdentificadorCliente() != null && !pedido.getPedIdentificadorCliente().isEmpty()) {
            dto.setNombreCliente(pedido.getPedIdentificadorCliente());

            // PRIORIDAD 3: Cliente desde Contacto
        } else if (pedido.getConId() != null) {
            contactoRepository.findById(pedido.getConId())
                    .ifPresent(c -> dto.setNombreCliente(c.getConNombre()));
        }

        // 🔥 NUEVA LÓGICA: ENRIQUECER NOMBRE DEL EMPLEADO
        if (pedido.getEmpleadoAsignado() != null) {
            dto.setNombreEmpleado(pedido.getEmpleadoAsignado().getUsuNombre());
        }

        return dto;
    }

    @Transactional(readOnly = true)
    public List<HistorialResponseDTO> obtenerHistorialPorPedido(Integer pedidoId) {
        return historialRepository.findHistorialConDetalles(pedidoId).stream()
                .map(HistorialMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // ========================================================================
    // MÉTODOS CRUD BÁSICOS Y COMPATIBILIDAD
    // ========================================================================

    @Transactional
    public PedidoResponseDTO guardarPedido(PedidoRequestDTO requestDTO, MultipartFile render) {
        // Implementación básica si se usa desde admin
        Pedido pedido = new Pedido();
        pedido.setPedFechaCreacion(new Date());

        // Asignación de comentarios
        pedido.setPedComentarios(requestDTO.getPedComentarios());

        // Generación de código
        pedido.setPedCodigo("ADM-" + System.currentTimeMillis());

        // Asignación de estado
        EstadoPedido estado = estadoPedidoRepository.findById(requestDTO.getEstId() != null ? requestDTO.getEstId() : 1)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));
        pedido.setEstadoPedido(estado);

        // LÓGICA DE ASIGNACIÓN DE CLIENTE
        if (requestDTO.getUsuIdCliente() != null) {
            // Cliente Registrado
            Usuario cliente = usuarioRepository.findById(requestDTO.getUsuIdCliente()).orElse(null);
            pedido.setCliente(cliente);
        } else if (requestDTO.getPedIdentificadorCliente() != null && !requestDTO.getPedIdentificadorCliente().isEmpty()) {
            // 🔥 SOLUCIÓN: Cliente Externo/Anónimo. Copiamos el dato del DTO a la Entidad.
            pedido.setPedIdentificadorCliente(requestDTO.getPedIdentificadorCliente());
        }

        Pedido guardado = pedidoRepository.save(pedido);

        // Crear primer historial (Opcional, pero recomendado para trazabilidad)
        HistorialEstadoPedido historial = new HistorialEstadoPedido();
        historial.setPedido(guardado);
        historial.setEstadoPedido(estado);
        historial.setHisFechaCambio(new Date());
        historial.setHisComentarios(requestDTO.getPedComentarios() + " (Pedido creado por Admin)");

        // Asumiendo que obtienes el responsable (Admin) del contexto o JWT aquí
        // Por simplicidad, omitimos la asignación de responsable en este ejemplo,
        // pero deberías añadirla.

        historialRepository.save(historial);

        return PedidoMapper.toPedidoResponseDTO(guardado);
    }

    @Transactional
    public PedidoResponseDTO actualizar(Integer id, PedidoRequestDTO requestDTO, MultipartFile render) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", "id", id));

        if(requestDTO.getPedComentarios() != null) pedido.setPedComentarios(requestDTO.getPedComentarios());
        if(requestDTO.getEstId() != null) {
            EstadoPedido estado = estadoPedidoRepository.findById(requestDTO.getEstId()).orElse(null);
            if(estado != null) pedido.setEstadoPedido(estado);
        }

        Pedido actualizado = pedidoRepository.save(pedido);
        return PedidoMapper.toPedidoResponseDTO(actualizado);
    }

    @Transactional
    public boolean eliminarPedido(Integer id) {
        if(pedidoRepository.existsById(id)) {
            pedidoRepository.deleteById(id);
            return true;
        }
        return false;
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
    public PedidoResponseDTO crearDesdeContacto(Integer contactoId, Integer estadoId, String comentarios, Integer usuIdEmpleado) {
        // Lógica placeholder si no se proporcionó antes
        return null;
    }

    @Transactional
    public PedidoResponseDTO asignarEmpleado(Integer pedidoId, Integer usuIdEmpleado, Integer responsableId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", "id", pedidoId));
        Usuario empleado = usuarioRepository.findById(usuIdEmpleado)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuIdEmpleado));

        pedido.setEmpleadoAsignado(empleado);
        Pedido pedidoGuardado = pedidoRepository.save(pedido); // <-- Guardamos la entidad

        // 🔥 CORRECCIÓN CLAVE: Convertimos y ENRIQUECEMOS el DTO antes de devolverlo.
        // La entidad PedidoGuardado tiene el objeto Empleado asignado,
        // pero necesitamos copiar el nombre al campo 'nombreEmpleado' del DTO.
        return convertirADTO(pedidoGuardado);
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> obtenerPedidosPorCliente(Integer usuIdCliente) {
        return pedidoRepository.findByClienteUsuId(usuIdCliente).stream()
                .map(this::convertirADTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> obtenerPedidosPorEmpleado(Integer usuIdEmpleado) {
        return pedidoRepository.findByEmpleadoAsignadoUsuId(usuIdEmpleado).stream()
                .map(this::convertirADTO).collect(Collectors.toList());
    }

}
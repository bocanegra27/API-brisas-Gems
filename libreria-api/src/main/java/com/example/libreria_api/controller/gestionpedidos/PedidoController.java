package com.example.libreria_api.controller.gestionpedidos;

import com.example.libreria_api.dto.gestionpedidos.HistorialResponseDTO;
import com.example.libreria_api.dto.gestionpedidos.PedidoRequestDTO;
import com.example.libreria_api.dto.gestionpedidos.PedidoResponseDTO;
import com.example.libreria_api.exception.ResourceNotFoundException;
import com.example.libreria_api.service.gestionpedidos.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@Tag(name="Pedidos",description = "Gestión integral del ciclo de vida de los pedidos, incluyendo " +
        "su creación, consulta y actualización de archivos adjuntos (renders).")
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    @Operation(summary = "Obtener lista de todos los pedidos",
    description = "Recupera una lista concisa de todos los pedidos registrados en el sistema.")
    public ResponseEntity<List<PedidoResponseDTO>> obtenerTodosLosPedidos() {
        List<PedidoResponseDTO> pedidos = pedidoService.obtenerTodosLosPedidos();
        return ResponseEntity.ok(pedidos);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Crear un nuevo pedido con archivo adjunto",
    description = "Registra un nuevo pedido. Requiere el envío de datos del pedido junto con un" +
            " archivo render opcional")
    public ResponseEntity<PedidoResponseDTO> guardarPedido(
            @ModelAttribute PedidoRequestDTO requestDTO,
            @RequestPart(value = "render", required = false) MultipartFile render) {

        PedidoResponseDTO nuevoPedido = pedidoService.guardarPedido(requestDTO, render);
        return new ResponseEntity<>(nuevoPedido, HttpStatus.CREATED);
    }

    /**
     * Endpoint auxiliar para crear pedidos manualmente SIN adjuntar un archivo (render).
     * Recibe JSON en el cuerpo.
     * POST /api/pedidos/json-manual
     */
    @PostMapping(value = "/json-manual", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PedidoResponseDTO> guardarPedidoJson(
            @RequestBody PedidoRequestDTO requestDTO) {

        // Llamamos al método de servicio principal, pasando 'null' para el archivo render.
        // Esto asegura que la lógica de validación y trazabilidad sea la misma.
        PedidoResponseDTO nuevoPedido = pedidoService.guardarPedido(requestDTO, null);

        return new ResponseEntity<>(nuevoPedido, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalles de pedido por ID",
    description = "Recupera los detalles completos, incluyendo información detallada del pedido específico " +
            "por su identificador.")
    public ResponseEntity<PedidoResponseDTO> obtenerPedidoPorId(@PathVariable Integer id) { // Cambia aquí
        PedidoResponseDTO pedido = pedidoService.obtenerPedidoPorId(id); // Y aquí
        return ResponseEntity.ok(pedido);
    }

    // 🔥 VERSIÓN ESTABLE Y CORREGIDA: Acepta PUT y POST (para el spoofing) y elimina 'consumes'
    // para aceptar ambos Content-Types (multipart/form-data Y urlencoded).
    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT, RequestMethod.POST})
    @Operation(summary = "Actualizar un pedido y su archivo (PUT/POST)",
    description = "Modifica los datos de un pedido existente identificado por su ID. Permite actualizar el archivo render opcionalmente. " +
            "Acepta PUT y POST para compatibilidad.")
    public ResponseEntity<PedidoResponseDTO> actualizar(
            @PathVariable Integer id,
            @ModelAttribute PedidoRequestDTO requestDTO,
            @RequestPart(value = "render", required = false) MultipartFile render) {

        PedidoResponseDTO pedidoActualizado = pedidoService.actualizar(id, requestDTO, render);

        if (pedidoActualizado == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(pedidoActualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un pedido",
    description = "Elimina permanentemente un pedido del sistema.")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Integer id) {
        if (pedidoService.eliminarPedido(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/count")
    @Operation(summary = "Contar pedidos por estado o total",
    description = "Devuelve el número total de pedidos, o el conteo filtrado" +
            " por un estado específico")
    public ResponseEntity<Map<String, Long>> contarPedidosPorEstado(
            @RequestParam(required = false) Integer estadoId) {

        long count;

        if (estadoId != null) {
            count = pedidoService.contarPedidosPorEstadoId(estadoId);
        } else {
            count = pedidoService.contarTotalPedidos();
        }

        return ResponseEntity.ok(Map.of("count", count));
    }

    // ==============================
// CREAR PEDIDO DESDE CONTACTO
// ==============================
    @PostMapping("/desde-contacto/{contactoId}")
    @Operation(summary = "Crear pedido a partir de un contacto",
    description = "Genera un nuevo pedido automáticamente utilizando la información " +
            "de un contacto existente. Permite definir el estado y comentarios iniciales.")
    public ResponseEntity<PedidoResponseDTO> crearPedidoDesdeContacto(
            @PathVariable Integer contactoId,
            @RequestParam(required = false) Integer estadoId,
            @RequestParam(required = false) String comentarios,
            @RequestParam(required = false) Integer usuIdEmpleado
    ) {
        PedidoResponseDTO pedido = pedidoService.crearDesdeContacto(
                contactoId,
                estadoId,
                comentarios,
                usuIdEmpleado
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

    // ==============================
    // CAMBIAR ESTADO (CON HISTORIAL)
    // ==============================
    @PatchMapping("/{id}/estado")
    public ResponseEntity<PedidoResponseDTO> cambiarEstado(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> payload) { // Usamos Map para JSON simple

        // Extracción de datos (asumiendo que Laravel envía {nuevoEstadoId, comentarios, responsableId})
        Integer nuevoEstadoId = (Integer) payload.get("nuevoEstadoId");
        String comentarios = (String) payload.get("comentarios");
        // 🔥 NOTA: El ID del responsable debe venir del JWT del usuario logueado, no del body.
        // Usaremos un placeholder (usu_id = 2, Pedro Paramo) hasta que se integre la seguridad.
        Integer responsableId = 2; // <<--- TEMPORAL: Reemplazar con lógica de JWT

        if (nuevoEstadoId == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            PedidoResponseDTO pedido = pedidoService.actualizarEstadoConHistorial(
                    id,
                    nuevoEstadoId,
                    comentarios,
                    responsableId
            );
            return ResponseEntity.ok(pedido);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ==============================
    // OBTENER HISTORIAL DE PEDIDO
    // ==============================
    /**
     * Devuelve el historial de cambios de estado para un pedido específico.
     * GET /api/pedidos/{id}/historial
     */
    @GetMapping("/{id}/historial")
    public ResponseEntity<List<HistorialResponseDTO>> obtenerHistorial(@PathVariable Integer id) {
        try {
            List<HistorialResponseDTO> historial = pedidoService.obtenerHistorialPorPedido(id);
            return ResponseEntity.ok(historial);
        } catch (Exception e) {
            // Manejar excepciones de manera controlada (ej: 404 si el pedido no existe)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{id}/asignar")
    public ResponseEntity<PedidoResponseDTO> asignarDisenador(
            @PathVariable Integer id,
            @RequestBody Map<String, Integer> payload) { // Recibe {usuIdEmpleado: X}

        Integer usuIdEmpleado = payload.get("usuIdEmpleado");

        // **NOTA:** El ID del responsable debe venir del JWT del admin logueado.
        // Usaremos un placeholder (Pedro Paramo=2) por ahora.
        Integer responsableId = 2;

        if (usuIdEmpleado == null) {
            return ResponseEntity.badRequest().build();
        }

        PedidoResponseDTO pedido = pedidoService.asignarEmpleado(
                id,
                usuIdEmpleado,
                responsableId
        );

        return ResponseEntity.ok(pedido);
    }

    @GetMapping("/cliente/{usuIdCliente}")
    @Operation(summary = "Obtener pedidos por ID de Cliente (Dashboard de Usuario)",
            description = "Recupera todos los pedidos creados por el cliente con el ID especificado.")
    // 🔥 Importante: Requiere que tu Spring Security tenga configurado Principal y Roles.
    // Esto permite que el cliente solo acceda a SUS pedidos.
    // Si aún no has configurado el JWT/Principal, puedes empezar con @PreAuthorize("permitAll()")
    // y cambiarlo más tarde, o usar un filtro simple en el Service.
    public ResponseEntity<List<PedidoResponseDTO>> obtenerPedidosPorCliente(@PathVariable Integer usuIdCliente) {
        List<PedidoResponseDTO> pedidos = pedidoService.obtenerPedidosPorCliente(usuIdCliente);
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/empleado/{usuIdEmpleado}")
    @Operation(summary = "Obtener pedidos asignados a un Empleado/Diseñador",
            description = "Recupera todos los pedidos que tienen asignado al empleado con el ID especificado.")
    // 🔥 Importante: Similar a arriba, requiere seguridad para que el diseñador solo vea sus asignaciones.
    public ResponseEntity<List<PedidoResponseDTO>> obtenerPedidosPorEmpleado(@PathVariable Integer usuIdEmpleado) {
        List<PedidoResponseDTO> pedidos = pedidoService.obtenerPedidosPorEmpleado(usuIdEmpleado);
        return ResponseEntity.ok(pedidos);
    }

    @PostMapping(value = "/{id}/estado-con-foto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PedidoResponseDTO> actualizarEstadoConFoto(
            @PathVariable Integer id,
            @RequestParam("nuevoEstadoId") Integer nuevoEstadoId,
            @RequestParam("comentarios") String comentarios,
            @RequestParam(value = "his_imagen", required = false) MultipartFile foto) {

        // El responsableId por ahora es 2 (Pedro Paramo) como en tus otros métodos
        Integer responsableId = 2;

        PedidoResponseDTO pedido = pedidoService.actualizarEstadoConHistorialYFoto(
                id, nuevoEstadoId, comentarios, responsableId, foto);

        return ResponseEntity.ok(pedido);
    }




}
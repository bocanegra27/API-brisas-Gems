package com.example.libreria_api.controller.gestionpedidos;

import com.example.libreria_api.dto.gestionpedidos.PedidoDetailResponseDTO;
import com.example.libreria_api.dto.gestionpedidos.PedidoRequestDTO;
import com.example.libreria_api.dto.gestionpedidos.PedidoResponseDTO;
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

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalles de pedido por ID",
    description = "Recupera los detalles completos, incluyendo información detallada del pedido específico " +
            "por su identificador.")
    public ResponseEntity<PedidoDetailResponseDTO> obtenerPedidoPorId(@PathVariable Integer id) {
        PedidoDetailResponseDTO pedido = pedidoService.obtenerPedidoPorId(id);
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
            @RequestParam(required = false) String comentarios
    ) {
        PedidoResponseDTO pedido = pedidoService.crearDesdeContacto(
                contactoId,
                estadoId,
                comentarios
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }
}
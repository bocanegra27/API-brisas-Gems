package com.example.libreria_api.controller.gestionpedidos;

import com.example.libreria_api.dto.gestionpedidos.HistorialResponseDTO;
import com.example.libreria_api.dto.gestionpedidos.PedidoRequestDTO;
import com.example.libreria_api.dto.gestionpedidos.PedidoResponseDTO;
import com.example.libreria_api.exception.ResourceNotFoundException;
import com.example.libreria_api.service.gestionpedidos.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@Tag(name="Pedidos", description = "Gestión integral del ciclo de vida de los pedidos.")
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    // ... (Mantén los métodos GET, POST de crear, DELETE, etc. igual que antes) ...
    // SOLO VOY A PONER EL MÉTODO QUE CAMBIA: cambiarEstado

    // ==============================
    // CAMBIAR ESTADO (CON HISTORIAL E IMAGEN)
    // ==============================
    @PatchMapping(value = "/{id}/estado", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Cambiar estado de pedido con evidencia",
            description = "Actualiza el estado y permite subir una imagen de evidencia para el historial.")
    public ResponseEntity<PedidoResponseDTO> cambiarEstado(
            @PathVariable Integer id,
            @RequestParam("nuevoEstadoId") Integer nuevoEstadoId,
            @RequestParam(value = "comentarios", required = false) String comentarios,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {

        Integer responsableId = 2; // TEMPORAL (Debe venir del JWT)

        if (nuevoEstadoId == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            PedidoResponseDTO pedido = pedidoService.actualizarEstadoConHistorial(
                    id,
                    nuevoEstadoId,
                    comentarios,
                    responsableId,
                    imagen // 🔥 Pasamos la imagen al servicio
            );
            return ResponseEntity.ok(pedido);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ... (El resto de métodos: historial, asignar, mis-pedidos, etc. se mantienen igual) ...

    // (Asegúrate de copiar el resto de tu archivo original aquí para no borrar los otros endpoints)
    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> obtenerTodosLosPedidos() {
        return ResponseEntity.ok(pedidoService.obtenerTodosLosPedidos());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PedidoResponseDTO> guardarPedido(@ModelAttribute PedidoRequestDTO requestDTO, @RequestPart(value = "render", required = false) MultipartFile render) {
        return new ResponseEntity<>(pedidoService.guardarPedido(requestDTO, render), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> obtenerPedidoPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(pedidoService.obtenerPedidoPorId(id));
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT, RequestMethod.POST})
    public ResponseEntity<PedidoResponseDTO> actualizar(@PathVariable Integer id, @ModelAttribute PedidoRequestDTO requestDTO, @RequestPart(value = "render", required = false) MultipartFile render) {
        PedidoResponseDTO pedidoActualizado = pedidoService.actualizar(id, requestDTO, render);
        return pedidoActualizado != null ? ResponseEntity.ok(pedidoActualizado) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Integer id) {
        return pedidoService.eliminarPedido(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> contarPedidosPorEstado(@RequestParam(required = false) Integer estadoId) {
        long count = (estadoId != null) ? pedidoService.contarPedidosPorEstadoId(estadoId) : pedidoService.contarTotalPedidos();
        return ResponseEntity.ok(Map.of("count", count));
    }

    @PostMapping("/desde-contacto/{contactoId}")
    public ResponseEntity<PedidoResponseDTO> crearPedidoDesdeContacto(@PathVariable Integer contactoId, @RequestParam(required = false) Integer estadoId, @RequestParam(required = false) String comentarios, @RequestParam(required = false) Integer usuIdEmpleado) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.crearDesdeContacto(contactoId, estadoId, comentarios, usuIdEmpleado));
    }

    @GetMapping("/{id}/historial")
    public ResponseEntity<List<HistorialResponseDTO>> obtenerHistorial(@PathVariable Integer id) {
        return ResponseEntity.ok(pedidoService.obtenerHistorialPorPedido(id));
    }

    @PatchMapping("/{id}/asignar")
    public ResponseEntity<PedidoResponseDTO> asignarDisenador(@PathVariable Integer id, @RequestBody Map<String, Integer> payload) {
        return ResponseEntity.ok(pedidoService.asignarEmpleado(id, payload.get("usuIdEmpleado"), 2));
    }

    @GetMapping("/cliente/{usuIdCliente}")
    public ResponseEntity<List<PedidoResponseDTO>> obtenerPedidosPorCliente(@PathVariable Integer usuIdCliente) {
        return ResponseEntity.ok(pedidoService.obtenerPedidosPorCliente(usuIdCliente));
    }

    @GetMapping("/empleado/{usuIdEmpleado}")
    public ResponseEntity<List<PedidoResponseDTO>> obtenerPedidosPorEmpleado(@PathVariable Integer usuIdEmpleado) {
        return ResponseEntity.ok(pedidoService.obtenerPedidosPorEmpleado(usuIdEmpleado));
    }

    @GetMapping("/mis-pedidos")
    public ResponseEntity<List<PedidoResponseDTO>> misPedidos(Principal principal) {
        return principal == null ? ResponseEntity.status(HttpStatus.UNAUTHORIZED).build() : ResponseEntity.ok(pedidoService.obtenerMisPedidos(principal.getName()));
    }

    @GetMapping("/mis-pedidos/{id}")
    public ResponseEntity<PedidoResponseDTO> miPedidoDetalle(@PathVariable Integer id, Principal principal) {
        if (principal == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        try {
            return ResponseEntity.ok(pedidoService.obtenerMiPedidoPorId(id, principal.getName()));
        } catch (RuntimeException e) {
            return e.getMessage().contains("ACCESO DENEGADO") ? ResponseEntity.status(HttpStatus.FORBIDDEN).build() : ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/mis-pedidos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PedidoResponseDTO> crearMiPedido(@Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)) @RequestPart("pedido") PedidoRequestDTO requestDTO, @RequestPart(value = "render", required = false) MultipartFile render, Principal principal) {
        if (principal == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return new ResponseEntity<>(pedidoService.crearMiPedido(requestDTO, render, principal.getName()), HttpStatus.CREATED);
    }
}
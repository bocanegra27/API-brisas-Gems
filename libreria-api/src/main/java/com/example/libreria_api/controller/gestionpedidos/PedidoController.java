package com.example.libreria_api.controller.gestionpedidos;

import com.example.libreria_api.dto.gestionpedidos.PedidoDetailResponseDTO;
import com.example.libreria_api.dto.gestionpedidos.PedidoRequestDTO;
import com.example.libreria_api.dto.gestionpedidos.PedidoResponseDTO;
import com.example.libreria_api.service.gestionpedidos.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> obtenerTodosLosPedidos() {
        List<PedidoResponseDTO> pedidos = pedidoService.obtenerTodosLosPedidos();
        return ResponseEntity.ok(pedidos);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PedidoResponseDTO> guardarPedido(
            @ModelAttribute PedidoRequestDTO requestDTO,
            @RequestPart(value = "render", required = false) MultipartFile render) {

        PedidoResponseDTO nuevoPedido = pedidoService.guardarPedido(requestDTO, render);
        return new ResponseEntity<>(nuevoPedido, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDetailResponseDTO> obtenerPedidoPorId(@PathVariable Integer id) {
        PedidoDetailResponseDTO pedido = pedidoService.obtenerPedidoPorId(id);
        return ResponseEntity.ok(pedido);
    }

    // 🔥 VERSIÓN ESTABLE Y CORREGIDA: Acepta PUT y POST (para el spoofing) y elimina 'consumes'
    // para aceptar ambos Content-Types (multipart/form-data Y urlencoded).
    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT, RequestMethod.POST})
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
    public ResponseEntity<Void> eliminarPedido(@PathVariable Integer id) {
        if (pedidoService.eliminarPedido(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/count")
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
    public ResponseEntity<PedidoResponseDTO> crearPedidoDesdeContacto(
            @PathVariable Integer contactoId,
            @RequestParam(required = false) Integer estadoId,
            @RequestParam(required = false) String comentarios,
            @RequestParam(required = false) Integer personalizacionId
    ) {
        PedidoResponseDTO pedido = pedidoService.crearDesdeContacto(
                contactoId,
                estadoId,
                comentarios,
                personalizacionId
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }
}
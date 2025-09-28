package com.example.libreria_api.controller.gestionpedidos;

import com.example.libreria_api.dto.gestionpedidos.PedidoDetailResponseDTO;
import com.example.libreria_api.dto.gestionpedidos.PedidoRequestDTO;
import com.example.libreria_api.dto.gestionpedidos.PedidoResponseDTO;
import com.example.libreria_api.service.gestionpedidos.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    // Endpoint para obtener la lista general
    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> obtenerTodosLosPedidos() {
        List<PedidoResponseDTO> pedidos = pedidoService.obtenerTodosLosPedidos();
        return ResponseEntity.ok(pedidos);
    }

    // Endpoint para crear un nuevo pedido
    @PostMapping
    public ResponseEntity<PedidoResponseDTO> crearPedido(@RequestBody PedidoRequestDTO requestDTO) {
        PedidoResponseDTO nuevoPedido = pedidoService.guardarPedido(requestDTO);
        return new ResponseEntity<>(nuevoPedido, HttpStatus.CREATED);
    }

    // --- CÓDIGO CLAVE CORREGIDO ---
    // Endpoint para obtener un pedido específico por ID
    @GetMapping("/{id}")
    public ResponseEntity<PedidoDetailResponseDTO> obtenerPedidoPorId(@PathVariable("id") Integer id) {
        PedidoDetailResponseDTO pedido = pedidoService.obtenerPedidoPorId(id);
        return ResponseEntity.ok(pedido);
    }

    // Endpoint para actualizar un pedido
    @PutMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> actualizar(@PathVariable("id") Integer id, @RequestBody PedidoRequestDTO requestDTO) {
        PedidoResponseDTO pedidoActualizado = pedidoService.actualizar(id, requestDTO);
        if (pedidoActualizado != null) {
            return ResponseEntity.ok(pedidoActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para eliminar un pedido
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable("id") Integer id) {
        boolean eliminado = pedidoService.eliminarPedido(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Long> contarPedidosPorEstado(@RequestParam String estado) {
        // Lo llamaremos desde PHP con /api/pedidos/count?estado=diseño
        return ResponseEntity.ok(pedidoService.contarPedidosPorEstado(estado));
    }
}
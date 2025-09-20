package com.example.libreria_api.controller.gestionpedidos;

import com.example.libreria_api.dto.gestionpedidos.EstadoPedidoRequestDTO;
import com.example.libreria_api.dto.gestionpedidos.EstadoPedidoResponseDTO;
import com.example.libreria_api.service.gestionpedidos.EstadoPedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estados-pedido") // Ruta base a nivel de clase
public class EstadoPedidoController {

    private final EstadoPedidoService estadoPedidoService;

     public EstadoPedidoController(EstadoPedidoService estadoPedidoService) {
        this.estadoPedidoService = estadoPedidoService;
    }

    @GetMapping
    public ResponseEntity<List<EstadoPedidoResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(estadoPedidoService.obtenerTodos());
    }

    @PostMapping
    public ResponseEntity<EstadoPedidoResponseDTO> crear(@RequestBody EstadoPedidoRequestDTO requestDTO) {
        EstadoPedidoResponseDTO nuevoEstado = estadoPedidoService.guardar(requestDTO);
        return new ResponseEntity<>(nuevoEstado, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadoPedidoResponseDTO> actualizar(@PathVariable Integer id, @RequestBody EstadoPedidoRequestDTO requestDTO) {
        EstadoPedidoResponseDTO estadoActualizado = estadoPedidoService.actualizar(id, requestDTO);
        if (estadoActualizado != null) {
            return ResponseEntity.ok(estadoActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
         estadoPedidoService.eliminar(id);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }
}
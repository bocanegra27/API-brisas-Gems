package com.example.libreria_api.controller.gestionpedidos;

import com.example.libreria_api.dto.gestionpedidos.EstadoPedidoRequestDTO;
import com.example.libreria_api.dto.gestionpedidos.EstadoPedidoResponseDTO;
import com.example.libreria_api.service.gestionpedidos.EstadoPedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/estados-pedido")
public class EstadoPedidoController {

    private final EstadoPedidoService estadoPedidoService;

    public EstadoPedidoController(EstadoPedidoService estadoPedidoService) {
        this.estadoPedidoService = estadoPedidoService;
    }

    @GetMapping
    public ResponseEntity<List<EstadoPedidoResponseDTO>> obtenerTodos() {
        try {
            List<EstadoPedidoResponseDTO> estados = estadoPedidoService.obtenerTodos();
            return ResponseEntity.ok(estados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoPedidoResponseDTO> obtenerPorId(@PathVariable Integer id) {
        try {
            Optional<EstadoPedidoResponseDTO> estado = estadoPedidoService.obtenerPorId(id);
            return estado.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<EstadoPedidoResponseDTO> obtenerPorNombre(@PathVariable String nombre) {
        try {
            Optional<EstadoPedidoResponseDTO> estado = estadoPedidoService.obtenerPorNombre(nombre);
            return estado.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody EstadoPedidoRequestDTO requestDTO) {
        try {
            EstadoPedidoResponseDTO nuevoEstado = estadoPedidoService.guardar(requestDTO);
            return new ResponseEntity<>(nuevoEstado, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody EstadoPedidoRequestDTO requestDTO) {
        try {
            EstadoPedidoResponseDTO estadoActualizado = estadoPedidoService.actualizar(id, requestDTO);
            if (estadoActualizado != null) {
                return ResponseEntity.ok(estadoActualizado);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            boolean eliminado = estadoPedidoService.eliminar(id);
            if (eliminado) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor");
        }
    }
}
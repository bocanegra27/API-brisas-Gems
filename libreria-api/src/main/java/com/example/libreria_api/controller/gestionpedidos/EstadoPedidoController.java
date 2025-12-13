package com.example.libreria_api.controller.gestionpedidos;

import com.example.libreria_api.dto.gestionpedidos.EstadoPedidoRequestDTO;
import com.example.libreria_api.dto.gestionpedidos.EstadoPedidoResponseDTO;
import com.example.libreria_api.service.gestionpedidos.EstadoPedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "Estados de Pedido", description = "Gestión del catálogo de los diferentes estados" +
        " por los que puede pasar un pedido")
@RequestMapping("/api/estados-pedido")
public class EstadoPedidoController {

    private final EstadoPedidoService estadoPedidoService;

    public EstadoPedidoController(EstadoPedidoService estadoPedidoService) {
        this.estadoPedidoService = estadoPedidoService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los estados de pedido",
    description = "Recupera la lista completa de todos los estados de pedido " +
            "configurados en el flujo de trabajo.")
    public ResponseEntity<List<EstadoPedidoResponseDTO>> obtenerTodos() {
        try {
            List<EstadoPedidoResponseDTO> estados = estadoPedidoService.obtenerTodos();
            return ResponseEntity.ok(estados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener estado por ID",
    description = "Busca y devuelve los detalles de un estado de pedido específico" +
            " utilizando su identificador numérico.")
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
    @Operation(summary = "Obtener estado de pedido",
    description = "Busca y devuelve los detalles de un estado de pedido específico utilizando su nombre único.")
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
    @Operation(summary = "Crear un nuevo estado de pedido", description = "Añade un nuevo estado " +
            "al flujo de trabajo de pedidos.")
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
    @Operation(summary = "Actualizar un estado de pedido",
    description = "Modifica el nombre o detalles de un estado de pedido existente identificado por su ID.")
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
    @Operation(summary = "Eliminar un estado de pedido",
    description = "Elimina un estado del catálogo. Nota: La eliminación puede fallar si el estado " +
            "está siendo usado por pedidos activos.")
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
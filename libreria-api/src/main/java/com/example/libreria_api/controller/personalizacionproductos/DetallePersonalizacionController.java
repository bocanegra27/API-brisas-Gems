package com.example.libreria_api.controller.personalizacionproductos;

import com.example.libreria_api.dto.personalizacionproductos.DetallePersonalizacionCreateDTO;
import com.example.libreria_api.dto.personalizacionproductos.DetallePersonalizacionUpdateDTO;
import com.example.libreria_api.dto.personalizacionproductos.DetallePersonalizacionResponseDTO;
import com.example.libreria_api.service.personalizacionproductos.DetallePersonalizacionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personalizaciones/{perId}/detalles")
public class DetallePersonalizacionController {

    private final DetallePersonalizacionService detalleService;

    public DetallePersonalizacionController(DetallePersonalizacionService detalleService) {
        this.detalleService = detalleService;
    }

    // ==============================
    // LISTAR DETALLES DE UNA PERSONALIZACIÓN
    // ==============================
    @GetMapping
    public List<DetallePersonalizacionResponseDTO> listar(@PathVariable int perId) {
        return detalleService.listarPorPersonalizacion(perId);
    }

    // ==============================
    // OBTENER DETALLE POR ID
    // ==============================
    @GetMapping("/{detId}")
    public ResponseEntity<DetallePersonalizacionResponseDTO> obtenerPorId(@PathVariable int perId,
                                                                          @PathVariable int detId) {
        try {
            DetallePersonalizacionResponseDTO dto = detalleService.obtenerPorId(detId);
            // Validar que el detalle pertenezca a la personalización
            if (!dto.getPerId().equals(perId)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(null);
            }
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // ==============================
    // CREAR DETALLE
    // ==============================
    @PostMapping
    public ResponseEntity<?> crear(@PathVariable int perId,
                                   @RequestBody DetallePersonalizacionCreateDTO dto) {
        try {
            DetallePersonalizacionResponseDTO creado = detalleService.crear(perId, dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // ==============================
    // ACTUALIZAR DETALLE
    // ==============================
    @PutMapping("/{detId}")
    public ResponseEntity<?> actualizar(@PathVariable int perId,
                                        @PathVariable int detId,
                                        @RequestBody DetallePersonalizacionUpdateDTO dto) {
        try {
            DetallePersonalizacionResponseDTO actualizado = detalleService.actualizar(detId, dto);
            if (!actualizado.getPerId().equals(perId)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El detalle no pertenece a la personalización especificada");
            }
            return ResponseEntity.ok(actualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // ==============================
    // ELIMINAR DETALLE
    // ==============================
    @DeleteMapping("/{detId}")
    public ResponseEntity<?> eliminar(@PathVariable int perId,
                                      @PathVariable int detId) {
        try {
            DetallePersonalizacionResponseDTO dto = detalleService.obtenerPorId(detId);
            if (!dto.getPerId().equals(perId)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El detalle no pertenece a la personalización especificada");
            }
            detalleService.eliminar(detId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}

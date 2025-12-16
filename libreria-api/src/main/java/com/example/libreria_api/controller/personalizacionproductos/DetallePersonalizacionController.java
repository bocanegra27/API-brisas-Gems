package com.example.libreria_api.controller.personalizacionproductos;

import com.example.libreria_api.dto.personalizacionproductos.DetallePersonalizacionCreateDTO;
import com.example.libreria_api.dto.personalizacionproductos.DetallePersonalizacionUpdateDTO;
import com.example.libreria_api.dto.personalizacionproductos.DetallePersonalizacionResponseDTO;
import com.example.libreria_api.service.personalizacionproductos.DetallePersonalizacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name="Detalles de Personalización", description = "Gestión de los valores " +
        "de personalización específicos elegidos por el cliente (Detalles) que " +
        "componen un registro de Personalización (perId) individual.")
@RequestMapping("/api/personalizaciones/{perId}/detalles")
public class DetallePersonalizacionController {

    private final DetallePersonalizacionService detalleService;

    public DetallePersonalizacionController(DetallePersonalizacionService detalleService) {
        this.detalleService = detalleService;
    }


    @GetMapping
    @Operation(summary = "Listar detalles de una personalización",
    description = "Recupera todos los detalles de personalización asociados a" +
            " un registro de Personalización padre específico")
    public List<DetallePersonalizacionResponseDTO> listar(@PathVariable int perId) {
        return detalleService.listarPorPersonalizacion(perId);
    }


    @GetMapping("/{detId}")
    @Operation(summary = "Obtener detalle específico por ID",
    description = "Busca y devuelve un detalle de personalización por su ID (detId), asegurando que " +
            "pertenezca al registro padre")
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

    @PostMapping
    @Operation(summary = "Crear nuevo detalle de personalización",
    description = "Añade un nuevo detalle de personalización (opción elegida " +
            "y su valor) al registro de Personalización padre")
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


    @PutMapping("/{detId}")
    @Operation(summary = "Actualizar un detalle de personalización",
    description = "Modifica un detalle de personalización existente (detId), verificando " +
            "su pertenencia al registro padre")
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


    @DeleteMapping("/{detId}")
    @Operation(summary = "Eliminar detalle de personalización",
    description = "Elimina un detalle específico (detId) del registro de Personalización padre")
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

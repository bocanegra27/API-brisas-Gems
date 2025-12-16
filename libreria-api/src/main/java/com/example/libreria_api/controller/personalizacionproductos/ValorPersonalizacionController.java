package com.example.libreria_api.controller.personalizacionproductos;

import com.example.libreria_api.dto.personalizacionproductos.ValorPersonalizacionCreateDTO;
import com.example.libreria_api.dto.personalizacionproductos.ValorPersonalizacionUpdateDTO;
import com.example.libreria_api.dto.personalizacionproductos.ValorPersonalizacionResponseDTO;
import com.example.libreria_api.service.personalizacionproductos.ValorPersonalizacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name="Valores de Personalización", description = "Gestión del catálogo de " +
        "los valores concretos que se pueden elegir dentro de una Opción de Personalización (ej: tallas, colores, materiales).")
@RequestMapping("/api/valores")
public class ValorPersonalizacionController {

    private final ValorPersonalizacionService valorService;

    public ValorPersonalizacionController(ValorPersonalizacionService valorService) {
        this.valorService = valorService;
    }


    @GetMapping
    @Operation(summary = "Listar y filtrar valores de personalizacion",
    description = "Recupera una lista de valores de personalización. " +
            "Permite filtrar por el ID de la Opción de Personalización (opcId) a la que pertenecen y por término de búsqueda (search).")
    public List<ValorPersonalizacionResponseDTO> listarValores(
            @RequestParam(required = false) Integer opcId,
            @RequestParam(required = false) String search) {
        return valorService.listar(opcId, search);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener valor por ID",
    description = "Busca y devuelve los detalles de un valor de " +
            "personalización específico utilizando su identificador.")
    public ResponseEntity<?> obtenerPorId(@PathVariable int id) {
        try {
            return ResponseEntity.ok(valorService.obtenerPorId(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @PostMapping
    @Operation(summary = "Crear un nuevo valor de personalización",
    description = "Registra un nuevo valor que podrá ser seleccionado por el cliente (ej: Metal, Plástico, Cuero).")
    public ResponseEntity<?> crearValor(@RequestBody ValorPersonalizacionCreateDTO dto) {
        try {
            ValorPersonalizacionResponseDTO creado = valorService.crear(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un valor de personalización",
    description = "Modifica los datos de un valor de personalización existente identificado por su ID.")
    public ResponseEntity<?> actualizarValor(@PathVariable int id,
                                             @RequestBody ValorPersonalizacionUpdateDTO dto) {
        try {
            return ResponseEntity.ok(valorService.actualizar(id, dto));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un valor de personalización",
    description = "Elimina permanentemente un valor del catálogo.")
    public ResponseEntity<?> eliminarValor(@PathVariable int id) {
        try {
            valorService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

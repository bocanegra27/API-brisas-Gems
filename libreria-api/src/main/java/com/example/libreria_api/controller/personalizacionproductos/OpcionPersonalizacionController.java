package com.example.libreria_api.controller.personalizacionproductos;

import com.example.libreria_api.dto.personalizacionproductos.OpcionPersonalizacionCreateDTO;
import com.example.libreria_api.dto.personalizacionproductos.OpcionPersonalizacionResponseDTO;
import com.example.libreria_api.dto.personalizacionproductos.OpcionPersonalizacionUpdateDTO;
import com.example.libreria_api.service.personalizacionproductos.OpcionPersonalizacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name="Opciones de Personalización", description = "Gestión del " +
        "catálogo de las categorías de personalización disponibles para los productos")
@RequestMapping("/api/opciones")
public class OpcionPersonalizacionController {

    private final OpcionPersonalizacionService opcionService;

    public OpcionPersonalizacionController(OpcionPersonalizacionService opcionService) {
        this.opcionService = opcionService;
    }

    // ==============================
    // LISTAR (con búsqueda opcional)
    // ==============================
    @GetMapping
    @Operation(summary = "Listar y buscar opciones de personalización",
    description = "Recupera una lista de las categorías de personalización. Permite la búsqueda opcional " +
            "por nombre o descripción")
    public List<OpcionPersonalizacionResponseDTO> listarOpciones(@RequestParam(required = false) String search) {
        return opcionService.listar(search);
    }

    // ==============================
    // OBTENER POR ID
    // ==============================
    @GetMapping("/{id}")
    @Operation(summary = "Obtener opción por ID",
    description = "Busca y devuelve los detalles de una opción de personalización específica" +
            " utilizando su identificador.")
    public ResponseEntity<OpcionPersonalizacionResponseDTO> obtenerPorId(@PathVariable int id) {
        try {
            return ResponseEntity.ok(opcionService.obtenerPorId(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // ==============================
    // CREAR
    // ==============================
    @PostMapping
    @Operation(summary = "Crear una nueva opción de personalización",
    description ="Registra una nueva categoría o tipo de personalización" )
    public ResponseEntity<?> crearOpcion(@RequestBody OpcionPersonalizacionCreateDTO dto) {
        try {
            OpcionPersonalizacionResponseDTO creada = opcionService.crear(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creada);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // ==============================
    // ACTUALIZAR
    // ==============================
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una opción de personalización",
    description = "Modifica los datos de una opción de personalización existente identificada por su ID.")
    public ResponseEntity<?> actualizarOpcion(@PathVariable int id,
                                              @RequestBody OpcionPersonalizacionUpdateDTO dto) {
        try {
            return ResponseEntity.ok(opcionService.actualizar(id, dto));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // ==============================
    // ELIMINAR
    // ==============================
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una opción de personalización",
    description = "Elimina permanentemente una opción del catálogo. " +
            "Nota: La eliminación puede fallar si existen valores de personalización asociados.")
    public ResponseEntity<?> eliminarOpcion(@PathVariable int id) {
        try {
            opcionService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
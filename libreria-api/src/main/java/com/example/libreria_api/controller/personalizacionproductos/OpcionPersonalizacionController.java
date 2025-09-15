package com.example.libreria_api.controller.personalizacionproductos;

import com.example.libreria_api.dto.personalizacionproductos.OpcionPersonalizacionCreateDTO;
import com.example.libreria_api.dto.personalizacionproductos.OpcionPersonalizacionResponseDTO;
import com.example.libreria_api.dto.personalizacionproductos.OpcionPersonalizacionUpdateDTO;
import com.example.libreria_api.service.personalizacionproductos.OpcionPersonalizacionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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
    public List<OpcionPersonalizacionResponseDTO> listarOpciones(@RequestParam(required = false) String search) {
        return opcionService.listar(search);
    }

    // ==============================
    // OBTENER POR ID
    // ==============================
    @GetMapping("/{id}")
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
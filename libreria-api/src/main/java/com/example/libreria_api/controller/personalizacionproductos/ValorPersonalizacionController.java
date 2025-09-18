package com.example.libreria_api.controller.personalizacionproductos;

import com.example.libreria_api.dto.personalizacionproductos.ValorPersonalizacionCreateDTO;
import com.example.libreria_api.dto.personalizacionproductos.ValorPersonalizacionUpdateDTO;
import com.example.libreria_api.dto.personalizacionproductos.ValorPersonalizacionResponseDTO;
import com.example.libreria_api.service.personalizacionproductos.ValorPersonalizacionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/valores")
public class ValorPersonalizacionController {

    private final ValorPersonalizacionService valorService;

    public ValorPersonalizacionController(ValorPersonalizacionService valorService) {
        this.valorService = valorService;
    }

    // ==============================
    // LISTAR (con filtros opcionales)
    // ==============================
    @GetMapping
    public List<ValorPersonalizacionResponseDTO> listarValores(
            @RequestParam(required = false) Integer opcId,
            @RequestParam(required = false) String search) {
        return valorService.listar(opcId, search);
    }

    // ==============================
    // OBTENER POR ID
    // ==============================
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable int id) {
        try {
            return ResponseEntity.ok(valorService.obtenerPorId(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ==============================
    // CREAR
    // ==============================
    @PostMapping
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

    // ==============================
    // ACTUALIZAR
    // ==============================
    @PutMapping("/{id}")
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

    // ==============================
    // ELIMINAR
    // ==============================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarValor(@PathVariable int id) {
        try {
            valorService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

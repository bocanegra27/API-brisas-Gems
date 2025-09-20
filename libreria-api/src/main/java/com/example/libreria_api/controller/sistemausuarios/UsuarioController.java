package com.example.libreria_api.controller.sistemausuarios;

import com.example.libreria_api.dto.sistemausuarios.*;
import com.example.libreria_api.service.sistemausuarios.UsuarioService;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    // ==============================
    // LISTAR con filtros y paginación
    // ==============================
    @GetMapping
    public Page<UsuarioResponseDTO> listarUsuarios(
            @RequestParam(required = false) Integer rolId,
            @RequestParam(required = false) Boolean activo,
            Pageable pageable) {
        return service.listarUsuarios(rolId, activo, pageable);
    }

    // ==============================
    // OBTENER POR ID
    // ==============================
    @GetMapping("/{usuId}")
    public UsuarioResponseDTO obtenerPorId(@PathVariable Integer usuId) {
        return service.obtenerPorId(usuId);
    }

    // ==============================
    // CREAR
    // ==============================
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crear(@Valid @RequestBody UsuarioCreateDTO dto) {
        UsuarioResponseDTO creado = service.crear(dto);
        return ResponseEntity.status(201).body(creado);
    }

    // ==============================
    // ACTUALIZAR
    // ==============================
    @PutMapping("/{usuId}")
    public UsuarioResponseDTO actualizar(@PathVariable Integer usuId,
                                         @Valid @RequestBody UsuarioUpdateDTO dto) {
        return service.actualizar(usuId, dto);
    }

    // ==============================
    // PATCH - Activar / Desactivar
    // ==============================
    @PatchMapping("/{usuId}/activo")
    public UsuarioResponseDTO actualizarActivo(@PathVariable Integer usuId,
                                               @RequestParam boolean activo) {
        UsuarioUpdateDTO dto = new UsuarioUpdateDTO();
        dto.setActivo(activo);
        return service.actualizar(usuId, dto);
    }

    // ==============================
    // PATCH - Cambiar Password
    // ==============================
    @PatchMapping("/{usuId}/password")
    public ResponseEntity<Void> actualizarPassword(@PathVariable Integer usuId,
                                                   @Valid @RequestBody PasswordUpdateDTO dto) {
        service.actualizarPassword(usuId, dto);
        return ResponseEntity.noContent().build();
    }

    // ==============================
    // ELIMINAR
    // ==============================
    @DeleteMapping("/{usuId}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer usuId) {
        service.eliminar(usuId);
        return ResponseEntity.noContent().build();
    }
}

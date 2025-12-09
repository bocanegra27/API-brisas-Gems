package com.example.libreria_api.controller.sistemausuarios;

import com.example.libreria_api.dto.sistemausuarios.SesionAnonimaCreateDTO;
import com.example.libreria_api.dto.sistemausuarios.SesionAnonimaResponseDTO;
import com.example.libreria_api.service.sistemausuarios.SesionAnonimaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sesiones-anonimas")
public class SesionAnonimaController {

    @Autowired
    private SesionAnonimaService sesionService;

    @PostMapping
    public ResponseEntity<SesionAnonimaResponseDTO> crear(@RequestBody(required = false) SesionAnonimaCreateDTO dto) {
        if (dto == null) {
            dto = new SesionAnonimaCreateDTO(); // DTO vacío
        }
        SesionAnonimaResponseDTO creada = sesionService.crearSesion(dto);
        return ResponseEntity.ok(creada);
    }

    @GetMapping("/{token}")
    public ResponseEntity<SesionAnonimaResponseDTO> obtenerPorToken(@PathVariable String token) {
        SesionAnonimaResponseDTO dto = sesionService.obtenerPorToken(token);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{token}/convertir")
    public ResponseEntity<SesionAnonimaResponseDTO> convertir(
            @PathVariable String token,
            @RequestParam Integer usuarioId) {
        try {
            SesionAnonimaResponseDTO convertida = sesionService.convertirSesion(token, usuarioId);
            return ResponseEntity.ok(convertida);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{token}/validar")
    public ResponseEntity<Boolean> validar(@PathVariable String token) {
        boolean valida = sesionService.validarSesion(token);
        return ResponseEntity.ok(valida);
    }
}
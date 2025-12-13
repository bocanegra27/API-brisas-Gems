package com.example.libreria_api.controller.sistemausuarios;

import com.example.libreria_api.dto.sistemausuarios.SesionAnonimaCreateDTO;
import com.example.libreria_api.dto.sistemausuarios.SesionAnonimaResponseDTO;
import com.example.libreria_api.service.sistemausuarios.SesionAnonimaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name="Sesiones Anónimas",description = "Gestión de las sesiones " +
        "temporales de usuarios no registrados o anónimos, típicamente usada " +
        "para seguimiento de carritos o preferencias.")
@RequestMapping("/api/sesiones-anonimas")
public class SesionAnonimaController {

    @Autowired
    private SesionAnonimaService sesionService;

    @PostMapping
    @Operation(summary = "Crear una nueva sesión anónima",
    description = "Inicia y registra una nueva sesión temporal y genera un token de seguimiento" +
            " para el usuario no autenticado.")
    public ResponseEntity<SesionAnonimaResponseDTO> crear(@RequestBody(required = false) SesionAnonimaCreateDTO dto) {
        if (dto == null) {
            dto = new SesionAnonimaCreateDTO(); // DTO vacío
        }
        SesionAnonimaResponseDTO creada = sesionService.crearSesion(dto);
        return ResponseEntity.ok(creada);
    }

    @GetMapping("/{token}")
    @Operation(summary = "Obtener sesión por token",
    description = "Recupera los detalles de una sesión anónima utilizando el token generado.")
    public ResponseEntity<SesionAnonimaResponseDTO> obtenerPorToken(@PathVariable String token) {
        SesionAnonimaResponseDTO dto = sesionService.obtenerPorToken(token);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{token}/convertir")
    @Operation(summary = "Convertir sesión anónima a usuario registrado",
    description = "Asocia una sesión anónima existente a un usuarioId registrado. " +
            "Usado típicamente después del login o registro para mantener la data del usuario.")
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
    @Operation(summary = "Validar la vigencia del token de sesión",
    description = "Verifica si el token de sesión anónima proporcionado " +
            "es válido y aún se encuentra activo.")
    public ResponseEntity<Boolean> validar(@PathVariable String token) {
        boolean valida = sesionService.validarSesion(token);
        return ResponseEntity.ok(valida);
    }
}
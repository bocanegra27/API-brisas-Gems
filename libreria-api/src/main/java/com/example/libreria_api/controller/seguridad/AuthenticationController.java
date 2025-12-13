package com.example.libreria_api.controller.seguridad;

import com.example.libreria_api.dto.seguridad.LoginRequestDTO;
import com.example.libreria_api.dto.seguridad.LoginResponseDTO;
import com.example.libreria_api.dto.seguridad.ResetPasswordDTO; // Asegúrate de tener este DTO creado
import com.example.libreria_api.service.seguridad.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


import java.util.Map;

@RestController
@Tag(name="Autenticacion  y seguridad", description = "Operaciones relacionadas con el inicio de sesión y " +
        "la generación de tokens de acceso (JWT).")
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión y obtener token JWT",
    description = "Permite a un usuario autenticarse con credenciales (usuario/email y contraseña) para recibir un " +
            "token de acceso (JWT) que deberá usar en futuras solicitudes protegidas.")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO request
    ) {
        LoginResponseDTO response = authenticationService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        if (email != null && !email.isEmpty()) {
            authenticationService.forgotPassword(email);
        }

        return ResponseEntity.ok().build();
    }

    // 🔥 NUEVO: Endpoint para restablecer la contraseña
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordDTO request) {
        authenticationService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok().build();
    }
}
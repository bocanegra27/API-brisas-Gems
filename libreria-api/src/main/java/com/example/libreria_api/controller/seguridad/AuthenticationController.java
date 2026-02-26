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
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO request) {
        try {
            LoginResponseDTO response = authenticationService.login(request);
            return ResponseEntity.ok(response);
        } catch (org.springframework.security.authentication.BadCredentialsException e) {
            return ResponseEntity.status(401)
                    .body(Map.of(
                            "error", "Unauthorized",
                            "message", "Credenciales incorrectas",
                            "status", 401,
                            "path", "/api/auth/login"
                    ));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        if (email != null && !email.isEmpty()) {
            authenticationService.forgotPassword(email);
        }

        return ResponseEntity.ok().build();
    }


    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordDTO request) {
        authenticationService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok().build();
    }
}
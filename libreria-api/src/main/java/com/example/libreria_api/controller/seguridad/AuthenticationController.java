package com.example.libreria_api.controller.seguridad;

import com.example.libreria_api.dto.seguridad.LoginRequestDTO;
import com.example.libreria_api.dto.seguridad.LoginResponseDTO;
import com.example.libreria_api.service.seguridad.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO request
    ) {
        LoginResponseDTO response = authenticationService.login(request);
        return ResponseEntity.ok(response);
    }
}
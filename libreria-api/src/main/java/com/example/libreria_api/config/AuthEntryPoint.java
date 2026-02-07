package com.example.libreria_api.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Maneja errores de autenticación (401 Unauthorized)
 * Se activa cuando:
 * - No hay token JWT
 * - El token es inválido o expirado
 * - Las credenciales de login son incorrectas
 */
@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);


        String jsonResponse = String.format(
                "{\"error\":\"Unauthorized\"," +
                        "\"message\":\"%s\"," +
                        "\"status\":401," +
                        "\"path\":\"%s\"}",
                authException.getMessage(),
                request.getRequestURI()
        );

        response.getWriter().write(jsonResponse);
    }
}
package com.example.libreria_api.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Maneja errores de autorización (403 Forbidden)
 * Se activa cuando:
 * - El usuario está autenticado PERO no tiene el rol necesario
 * - Intenta acceder a un recurso sin permisos
 */
@Component
public class AccessDenied implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);


        String jsonResponse = String.format(
                "{\"error\":\"Forbidden\"," +
                        "\"message\":\"Acceso denegado. No tienes los permisos necesarios.\"," +
                        "\"status\":403," +
                        "\"path\":\"%s\"}",
                request.getRequestURI()
        );

        response.getWriter().write(jsonResponse);
    }
}
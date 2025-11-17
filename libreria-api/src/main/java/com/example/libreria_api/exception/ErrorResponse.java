package com.example.libreria_api.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Estructura estándar para todas las respuestas de error de la API
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // No incluye campos null en el JSON
public class ErrorResponse {

    private String error;           // Nombre del error (ej: "Bad Request")
    private String message;         // Mensaje descriptivo
    private Integer status;         // Código HTTP (400, 404, etc.)
    private String path;            // Ruta de la petición

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    private Map<String, String> errors; // Para validaciones (campo → mensaje)

    public static ErrorResponse of(String error, String message, Integer status, String path) {
        return ErrorResponse.builder()
                .error(error)
                .message(message)
                .status(status)
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
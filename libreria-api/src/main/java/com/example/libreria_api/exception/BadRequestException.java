package com.example.libreria_api.exception;

/**
 * Excepción lanzada cuando los datos de entrada son inválidos
 * Ejemplo: Email con formato incorrecto, campos requeridos vacíos
 * Se mapea a HTTP 400 Bad Request
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
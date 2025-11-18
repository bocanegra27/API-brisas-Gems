package com.example.libreria_api.exception;

/**
 * Excepción lanzada cuando se intenta crear un recurso que ya existe
 * Ejemplo: Crear usuario con email duplicado
 * Se mapea a HTTP 409 Conflict
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String recurso, String campo, Object valor) {
        super(String.format("%s ya existe con %s: '%s'", recurso, campo, valor));
    }
}
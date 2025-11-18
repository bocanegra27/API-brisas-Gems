package com.example.libreria_api.exception;

/**
 * Excepción lanzada cuando un recurso no se encuentra en la base de datos
 * Ejemplo: Buscar un pedido con ID que no existe
 * Se mapea a HTTP 404 Not Found
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String recurso, String campo, Object valor) {
        super(String.format("%s no encontrado con %s: '%s'", recurso, campo, valor));
    }
}
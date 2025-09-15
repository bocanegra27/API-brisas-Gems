package com.example.libreria_api.dto.personalizacionproductos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class OpcionPersonalizacionCreateDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String nombre;

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}

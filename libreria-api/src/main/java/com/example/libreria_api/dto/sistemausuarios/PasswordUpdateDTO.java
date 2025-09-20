package com.example.libreria_api.dto.sistemausuarios;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PasswordUpdateDTO {

    @NotBlank
    private String passwordActual;

    @NotBlank
    @Size(min = 8)
    private String passwordNueva;

    // Constructor vacío
    public PasswordUpdateDTO() {
    }

    // Constructor completo
    public PasswordUpdateDTO(String passwordActual, String passwordNueva) {
        this.passwordActual = passwordActual;
        this.passwordNueva = passwordNueva;
    }

    // Getters y Setters
    public String getPasswordActual() { return passwordActual; }
    public void setPasswordActual(String passwordActual) { this.passwordActual = passwordActual; }

    public String getPasswordNueva() { return passwordNueva; }
    public void setPasswordNueva(String passwordNueva) { this.passwordNueva = passwordNueva; }
}

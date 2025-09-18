package com.example.libreria_api.dto.personalizacionproductos;

import jakarta.validation.constraints.NotNull;

public class DetallePersonalizacionUpdateDTO {

    @NotNull(message = "El valId es obligatorio")
    private Integer valId;

    // Getters y Setters
    public Integer getValId() {
        return valId;
    }

    public void setValId(Integer valId) {
        this.valId = valId;
    }
}

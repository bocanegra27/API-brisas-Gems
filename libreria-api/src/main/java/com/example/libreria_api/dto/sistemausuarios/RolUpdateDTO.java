package com.example.libreria_api.dto.sistemausuarios;

import jakarta.validation.constraints.NotNull;

/**
 * DTO específico para recibir la actualización del ID del rol.
 * Será usado como el @RequestBody de la petición PATCH /usuarios/{id}/rol.
 */
public class RolUpdateDTO {

    @NotNull(message = "El ID del rol es obligatorio")
    private Integer rolId;


    public RolUpdateDTO() {}


    public Integer getRolId() {
        return rolId;
    }

    public void setRolId(Integer rolId) {
        this.rolId = rolId;
    }
}
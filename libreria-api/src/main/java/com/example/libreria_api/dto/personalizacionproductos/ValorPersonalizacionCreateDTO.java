package com.example.libreria_api.dto.personalizacionproductos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ValorPersonalizacionCreateDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    private String nombre;

    @Size(max = 250, message = "La URL de la imagen no puede superar 250 caracteres")
    private String imagen;

    @NotNull(message = "El ID de la opción es obligatorio")
    private Integer opcId;

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public Integer getOpcId() { return opcId; }
    public void setOpcId(Integer opcId) { this.opcId = opcId; }
}

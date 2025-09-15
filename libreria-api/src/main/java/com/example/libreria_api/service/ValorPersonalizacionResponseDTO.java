package com.example.libreria_api.service;

public class ValorPersonalizacionResponseDTO {

    private Integer id;
    private String nombre;
    private String imagen;
    private Integer opcId;
    private String opcionNombre; // opcional, para enriquecer la respuesta

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public Integer getOpcId() { return opcId; }
    public void setOpcId(Integer opcId) { this.opcId = opcId; }

    public String getOpcionNombre() { return opcionNombre; }
    public void setOpcionNombre(String opcionNombre) { this.opcionNombre = opcionNombre; }
}


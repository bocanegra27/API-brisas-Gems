package com.example.libreria_api.service;

public class OpcionPersonalizacionResponseDTO {

    private int id;
    private String nombre;

    // Constructor
    public OpcionPersonalizacionResponseDTO(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
}

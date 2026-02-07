package com.example.libreria_api.dto.personalizacionproductos;

public class OpcionPersonalizacionResponseDTO {

    private int id;
    private String nombre;
    private Integer catId;

    // Constructor
    public OpcionPersonalizacionResponseDTO(int id, String nombre, Integer catId) {
        this.id = id;
        this.nombre = nombre;
        this.catId = catId;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getCatId() { return catId; }

    public void setCatId(Integer catId) { this.catId = catId; }
}

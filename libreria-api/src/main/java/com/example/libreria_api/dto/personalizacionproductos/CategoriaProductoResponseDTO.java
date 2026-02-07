package com.example.libreria_api.dto.personalizacionproductos;

public class CategoriaProductoResponseDTO {
    private Integer id;
    private String nombre;
    private String slug;

    public CategoriaProductoResponseDTO(Integer id, String nombre, String slug) {
        this.id = id;
        this.nombre = nombre;
        this.slug = slug;
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
}
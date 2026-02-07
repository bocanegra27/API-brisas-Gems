package com.example.libreria_api.model.personalizacionproductos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "categoria_producto")
public class CategoriaProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cat_id")
    private Integer catId;

    @Column(name = "cat_nombre", nullable = false, unique = true, length = 100)
    private String catNombre;

    @Column(name = "cat_slug", unique = true, length = 100)
    private String catSlug;

    // Relación inversa (Una categoría tiene muchas opciones)
    // Usamos JsonIgnore para que al pedir la categoría no traiga recursivamente todo el árbol infinito
    @OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<OpcionPersonalizacion> opciones;

    public CategoriaProducto() {}

    public Integer getCatId() { return catId; }
    public void setCatId(Integer catId) { this.catId = catId; }

    public String getCatNombre() { return catNombre; }
    public void setCatNombre(String catNombre) { this.catNombre = catNombre; }

    public String getCatSlug() { return catSlug; }
    public void setCatSlug(String catSlug) { this.catSlug = catSlug; }

    public List<OpcionPersonalizacion> getOpciones() { return opciones; }
    public void setOpciones(List<OpcionPersonalizacion> opciones) { this.opciones = opciones; }
}
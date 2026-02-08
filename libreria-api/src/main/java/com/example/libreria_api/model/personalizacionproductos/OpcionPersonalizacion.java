package com.example.libreria_api.model.personalizacionproductos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "opcion_personalizacion")
public class OpcionPersonalizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "opc_id")
    private int opcId;

    @Column(name = "opc_nombre", nullable = false, unique = true, length = 100)
    private String opcNombre;

    // Relación con valor_personalizacion
    @OneToMany(mappedBy = "opcionPersonalizacion", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    @OrderBy("valNombre ASC")
    private List<ValorPersonalizacion> valores;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cat_id", nullable = false)
    @JsonIgnore // Evita que al serializar la opción se intente serializar la categoría completa
    private CategoriaProducto categoria;

    public OpcionPersonalizacion() {}


    public OpcionPersonalizacion(String opcNombre) {
        this.opcNombre = opcNombre;
    }


    public int getOpcId() {
        return opcId;
    }

    public void setOpcId(int opcId) {
        this.opcId = opcId;
    }

    public String getOpcNombre() {
        return opcNombre;
    }

    public void setOpcNombre(String opcNombre) {
        this.opcNombre = opcNombre;
    }

    public List<ValorPersonalizacion> getValores() {
        return valores;
    }

    public void setValores(List<ValorPersonalizacion> valores) {
        this.valores = valores;
    }

    public CategoriaProducto getCategoria() { return categoria; }

    public void setCategoria(CategoriaProducto categoria) { this.categoria = categoria; }
}
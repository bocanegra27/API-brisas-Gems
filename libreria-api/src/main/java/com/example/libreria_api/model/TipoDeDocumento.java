package com.example.libreria_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tipo_de_documento")
public class TipoDeDocumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tipdoc_id;

    @Column(name = "tipdoc_nombre")
    private String tipdocNombre;

    // Constructor vacío
    public TipoDeDocumento() {
    }

    // Getters y Setters
    public int getTipdoc_id() {
        return tipdoc_id;
    }

    public void setTipdoc_id(int tipdoc_id) {
        this.tipdoc_id = tipdoc_id;
    }

    public String getTipdocNombre() {
        return tipdocNombre;
    }

    public void setTipdocNombre(String tipdocNombre) {
        this.tipdocNombre = tipdocNombre;
    }
}
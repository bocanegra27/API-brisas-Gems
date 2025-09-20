package com.example.libreria_api.model.sistemausuarios;

import jakarta.persistence.*;

@Entity
@Table(name = "tipo_de_documento")
public class TipoDeDocumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tipdocId;

    @Column(name = "tipdoc_nombre")
    private String tipdocNombre;

    // Constructor vacío
    public TipoDeDocumento() {
    }

    // Getters y Setters
    public int getTipdocId() {
        return tipdocId;
    }

    public void setTipdocId(int tipdoc_id) {
        this.tipdocId = tipdoc_id;
    }

    public String getTipdocNombre() {
        return tipdocNombre;
    }

    public void setTipdocNombre(String tipdocNombre) {
        this.tipdocNombre = tipdocNombre;
    }
}
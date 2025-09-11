package com.example.libreria_api.model.personalizacionproductos;

import jakarta.persistence.*;

@Entity
@Table (name= "personalizacion")
public class Personalizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int per_id;

    @Column(name = "per_fecha")
    private String perFecha;

    @Column(name = "usu_id")
    private Integer usuId;

    public Personalizacion() {
    }

    public Personalizacion(int per_id, String perFecha, Integer usuId) {
        this.per_id = per_id;
        this.perFecha = perFecha;
        this.usuId = usuId;
    }

    public int getPer_id() {
        return per_id;
    }

    public void setPer_id(int per_id) {
        this.per_id = per_id;
    }

    public String getPerFecha() {
        return perFecha;
    }

    public void setPerFecha(String perFecha) {
        this.perFecha = perFecha;
    }

    public Integer getUsuId() {
        return usuId;
    }

    public void setUsuId(Integer usuId) {
        this.usuId = usuId;
    }
}



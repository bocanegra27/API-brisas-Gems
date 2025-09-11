package com.example.libreria_api.model.personalizacionproductos;

import jakarta.persistence.*;

@Entity
@Table(name = "opcion_personalizacion")
public class OpcionPersonalizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int opc_id;

    @Column(name = "opc_nombre")
    private String opcNombre;

    public OpcionPersonalizacion() {
    }

    // Getters y Setters
    public int getOpc_id() {
        return opc_id;
    }

    public void setOpc_id(int opc_id) {
        this.opc_id = opc_id;
    }

    public String getOpcNombre() {
        return opcNombre;
    }

    public void setOpcNombre(String opcNombre) {
        this.opcNombre = opcNombre;
    }
}
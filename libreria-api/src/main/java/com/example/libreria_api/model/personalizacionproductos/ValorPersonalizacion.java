package com.example.libreria_api.model.personalizacionproductos;

import com.example.libreria_api.model.personalizacionproductos.OpcionPersonalizacion;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "valor_personalizacion")
public class ValorPersonalizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "val_id")
    private Integer valId;

    @Column(name = "val_nombre", nullable = false, length = 100)
    private String valNombre;

    @Column(name = "val_imagen", length = 250)
    private String valImagen;

    // Relación con opcion_personalizacion
    @ManyToOne(fetch = FetchType.LAZY, optional = false) // Mejor poner obligatorio
    @JoinColumn(name = "opc_id", nullable = false)       // reflejamos la FK como NOT NULL
    @JsonBackReference  // evita bucle en JSON (opción -> valores -> opción -> ...)
    private OpcionPersonalizacion opcionPersonalizacion;

    // --- Constructores ---
    public ValorPersonalizacion() {
    }

    public ValorPersonalizacion(String valNombre, String valImagen, OpcionPersonalizacion opcionPersonalizacion) {
        this.valNombre = valNombre;
        this.valImagen = valImagen;
        this.opcionPersonalizacion = opcionPersonalizacion;
    }

    // --- Getters y Setters ---
    public Integer getValId() {
        return valId;
    }

    public void setValId(Integer valId) {
        this.valId = valId;
    }

    public String getValNombre() {
        return valNombre;
    }

    public void setValNombre(String valNombre) {
        this.valNombre = valNombre;
    }

    public String getValImagen() {
        return valImagen;
    }

    public void setValImagen(String valImagen) {
        this.valImagen = valImagen;
    }

    public OpcionPersonalizacion getOpcionPersonalizacion() {
        return opcionPersonalizacion;
    }

    public void setOpcionPersonalizacion(OpcionPersonalizacion opcionPersonalizacion) {
        this.opcionPersonalizacion = opcionPersonalizacion;
    }
}

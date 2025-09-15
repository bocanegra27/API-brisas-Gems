package com.example.libreria_api.model.personalizacionproductos;

import com.example.libreria_api.model.personalizacionproductos.OpcionPersonalizacion;
import jakarta.persistence.*;

@Entity
@Table(name = "valor_personalizacion")
public class ValorPersonalizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "val_id")
    private int valId;

    @Column(name = "val_nombre", nullable = false, length = 100)
    private String valNombre;

    @Column(name = "val_imagen", length = 250)
    private String valImagen;


    @ManyToOne
    @JoinColumn(name = "opc_id", nullable = true)
    private OpcionPersonalizacion opcionPersonalizacion;


    public ValorPersonalizacion() {
    }

    public ValorPersonalizacion(String valNombre, String valImagen, OpcionPersonalizacion opcionPersonalizacion) {
        this.valNombre = valNombre;
        this.valImagen = valImagen;
        this.opcionPersonalizacion = opcionPersonalizacion;
    }


    public int getValId() {
        return valId;
    }

    public void setValId(int valId) {
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

package com.example.libreria_api.model.gestionpedidos;

import jakarta.persistence.*;

@Entity
@Table(name = "estado_pedido")
public class EstadoPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int est_id;

    @Column(name = "est_nombre", nullable = false, length = 50)
    private String estNombre;

    @Column(name = "est_descripcion", length = 200)
    private String estDescripcion;


    public EstadoPedido() {
    }

    public EstadoPedido(int est_id, String estNombre, String estDescripcion) {
        this.est_id = est_id;
        this.estNombre = estNombre;
        this.estDescripcion = estDescripcion;
    }


    public int getEst_id() {
        return est_id;
    }

    public void setEst_id(int est_id) {
        this.est_id = est_id;
    }

    public String getEstNombre() {
        return estNombre;
    }

    public void setEstNombre(String estNombre) {
        this.estNombre = estNombre;
    }

    public String getEstDescripcion() {
        return estDescripcion;
    }

    public void setEstDescripcion(String estDescripcion) {
        this.estDescripcion = estDescripcion;
    }

    @Override
    public String toString() {
        return "EstadoPedido{" +
                "est_id=" + est_id +
                ", estNombre='" + estNombre + '\'' +
                ", estDescripcion='" + estDescripcion + '\'' +
                '}';
    }
}
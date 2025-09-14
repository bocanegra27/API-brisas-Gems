package com.example.libreria_api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "personalizacion")
public class Personalizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "per_id")
    private int perId;

    @Column(name = "per_fecha", nullable = false)
    private LocalDate perFecha;

    @ManyToOne
    @JoinColumn(name = "usu_id", referencedColumnName = "usu_id")
    private Usuario usuario;

    /*@OneToMany(mappedBy = "personalizacion", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<DetallePersonalizacion> detalles;*/


    // 🚨 Parche temporal: relación sin mappedBy
    @OneToMany //(mappedBy = "personalizacion", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Pedido> pedidos;

    public Personalizacion() {
    }

    public Personalizacion(int perId) {
        this.perId = perId;
    }

    public Personalizacion(int perId, LocalDate perFecha, Usuario usuario) {
        this.perFecha = perFecha;
        this.usuario = usuario;
    }

    public int getPerId() {
        return perId;
    }

    public LocalDate getPerFecha() {
        return perFecha;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    /*public List<DetallePersonalizacion> getDetalles() {
        return detalles;
    }*/

    public void setPerId(int perId) {
        this.perId = perId;
    }

    public void setPerFecha(LocalDate perFecha) {
        this.perFecha = perFecha;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /*public void setDetalles(List<DetallePersonalizacion> detalles) {
        this.detalles = detalles;
    }*/

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }
}



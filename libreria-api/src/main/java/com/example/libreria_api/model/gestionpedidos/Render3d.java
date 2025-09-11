package com.example.libreria_api.model.gestionpedidos;

import com.example.libreria_api.model.gestionpedidos.Pedido;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "render_3d")
public class Render3d {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ren_id;

    @Column(name = "ren_imagen", nullable = false, length = 100)
    private String renImagen;

    @Column(name = "ren_fecha_aprobacion")
    private LocalDate renFechaAprobacion;

    @ManyToOne
    @JoinColumn(name = "ped_id")
    private Pedido pedido;

    // --- Constructores ---
    public Render3d() {
    }

    public Render3d(String renImagen, LocalDate renFechaAprobacion, Pedido pedido) {
        this.renImagen = renImagen;
        this.renFechaAprobacion = renFechaAprobacion;
        this.pedido = pedido;
    }

    // --- Getters y Setters ---
    public int getRen_id() {
        return ren_id;
    }

    public void setRen_id(int ren_id) {
        this.ren_id = ren_id;
    }

    public String getRenImagen() {
        return renImagen;
    }

    public void setRenImagen(String renImagen) {
        this.renImagen = renImagen;
    }

    public LocalDate getRenFechaAprobacion() {
        return renFechaAprobacion;
    }

    public void setRenFechaAprobacion(LocalDate renFechaAprobacion) {
        this.renFechaAprobacion = renFechaAprobacion;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
}
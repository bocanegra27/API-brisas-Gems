package com.example.libreria_api.model.gestionpedidos;

import com.example.libreria_api.model.gestionpedidos.Pedido;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "foto_producto_final")
public class FotoProductoFinal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int fot_id;

    @Column(name = "fot_imagen_final", nullable = false, length = 250)
    private String fotImagenFinal;

    @Column(name = "fot_fecha_subida")
    private LocalDate fotFechaSubida;

    @ManyToOne
    @JoinColumn(name = "ped_id")
    private Pedido pedido;

    // --- Constructores ---

    public FotoProductoFinal() {
    }

    public FotoProductoFinal(String fotImagenFinal, LocalDate fotFechaSubida, Pedido pedido) {
        this.fotImagenFinal = fotImagenFinal;
        this.fotFechaSubida = fotFechaSubida;
        this.pedido = pedido;
    }

    // --- Getters y Setters ---

    public int getFot_id() {
        return fot_id;
    }

    public void setFot_id(int fot_id) {
        this.fot_id = fot_id;
    }

    public String getFotImagenFinal() {
        return fotImagenFinal;
    }

    public void setFotImagenFinal(String fotImagenFinal) {
        this.fotImagenFinal = fotImagenFinal;
    }

    public LocalDate getFotFechaSubida() {
        return fotFechaSubida;
    }

    public void setFotFechaSubida(LocalDate fotFechaSubida) {
        this.fotFechaSubida = fotFechaSubida;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
}
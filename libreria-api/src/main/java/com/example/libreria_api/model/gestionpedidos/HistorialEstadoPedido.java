package com.example.libreria_api.model.gestionpedidos;

import com.example.libreria_api.model.sistemausuarios.Usuario;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "historial_estado_pedido")
public class HistorialEstadoPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "his_id")
    private Integer hisId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ped_id", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "est_id", nullable = false)
    private EstadoPedido estadoPedido;

    @Column(name = "his_fecha_cambio", nullable = false)
    private Date hisFechaCambio;

    @Column(name = "his_comentarios", columnDefinition = "TEXT")
    private String hisComentarios;

    @Column(name = "his_imagen", length = 250)
    private String hisImagen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usu_id_responsable")
    private Usuario usuarioResponsable;



    public HistorialEstadoPedido() {
        this.hisFechaCambio = new Date();
    }

    // Getters y Setters
    public Integer getHisId() {
        return hisId;
    }

    public void setHisId(Integer hisId) {
        this.hisId = hisId;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public EstadoPedido getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(EstadoPedido estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public Date getHisFechaCambio() {
        return hisFechaCambio;
    }

    public void setHisFechaCambio(Date hisFechaCambio) {
        this.hisFechaCambio = hisFechaCambio;
    }

    public String getHisComentarios() {
        return hisComentarios;
    }

    public void setHisComentarios(String hisComentarios) {
        this.hisComentarios = hisComentarios;
    }

    public String getHisImagen() {
        return hisImagen;
    }

    public void setHisImagen(String hisImagen) {
        this.hisImagen = hisImagen;
    }

    public Usuario getUsuarioResponsable() {
        return usuarioResponsable;
    }

    public void setUsuarioResponsable(Usuario usuarioResponsable) {
        this.usuarioResponsable = usuarioResponsable;
    }
}
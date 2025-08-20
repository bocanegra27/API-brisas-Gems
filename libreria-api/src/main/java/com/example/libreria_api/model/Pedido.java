package com.example.libreria_api.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ped_id;

    @Column(name = "ped_codigo")
    private String pedCodigo;

    @Column(name = "ped_fecha_creacion")
    private Date pedFechaCreacion;

    @Column(name = "ped_comentarios")
    private String pedComentarios;

    @Column(name = "est_id")
    private Integer estId;

    @Column(name = "per_id")
    private Integer perId;

    @Column(name = "usu_id")
    private Integer usuId;

    public Pedido() {
    }

    // Getters y Setters
    public int getPed_id() { return ped_id; }
    public void setPed_id(int ped_id) { this.ped_id = ped_id; }
    public String getPedCodigo() { return pedCodigo; }
    public void setPedCodigo(String pedCodigo) { this.pedCodigo = pedCodigo; }
    public Date getPedFechaCreacion() { return pedFechaCreacion; }
    public void setPedFechaCreacion(Date pedFechaCreacion) { this.pedFechaCreacion = pedFechaCreacion; }
    public String getPedComentarios() { return pedComentarios; }
    public void setPedComentarios(String pedComentarios) { this.pedComentarios = pedComentarios; }
    public Integer getEstId() { return estId; }
    public void setEstId(Integer estId) { this.estId = estId; }
    public Integer getPerId() { return perId; }
    public void setPerId(Integer perId) { this.perId = perId; }
    public Integer getUsuId() { return usuId; }
    public void setUsuId(Integer usuId) { this.usuId = usuId; }
}
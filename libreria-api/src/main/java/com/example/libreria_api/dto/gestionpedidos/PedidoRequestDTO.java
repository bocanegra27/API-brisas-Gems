package com.example.libreria_api.dto.gestionpedidos;

public class PedidoRequestDTO {

    private String pedCodigo;
    private String pedComentarios;
    private Integer estId;
    private Integer perId;
    private Integer usuId;

    // Getters y Setters
    public String getPedCodigo() {
        return pedCodigo;
    }

    public void setPedCodigo(String pedCodigo) {
        this.pedCodigo = pedCodigo;
    }

    public String getPedComentarios() {
        return pedComentarios;
    }

    public void setPedComentarios(String pedComentarios) {
        this.pedComentarios = pedComentarios;
    }

    public Integer getEstId() {
        return estId;
    }

    public void setEstId(Integer estId) {
        this.estId = estId;
    }

    public Integer getPerId() {
        return perId;
    }

    public void setPerId(Integer perId) {
        this.perId = perId;
    }

    public Integer getUsuId() {
        return usuId;
    }

    public void setUsuId(Integer usuId) {
        this.usuId = usuId;
    }
}
package com.example.libreria_api.dto.gestionpedidos;

public class PedidoRequestDTO {

    private String pedComentarios;
    private Integer estId;
    private Integer perId;
    private Integer usuId;

    // NUEVOS CAMPOS para sesiones anónimas
    private Integer sesionId;
    private Integer conId;
    private String pedIdentificadorCliente;

    // Getters y Setters existentes
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

    // NUEVOS GETTERS Y SETTERS
    public Integer getSesionId() {
        return sesionId;
    }

    public void setSesionId(Integer sesionId) {
        this.sesionId = sesionId;
    }

    public Integer getConId() {
        return conId;
    }

    public void setConId(Integer conId) {
        this.conId = conId;
    }

    public String getPedIdentificadorCliente() {
        return pedIdentificadorCliente;
    }

    public void setPedIdentificadorCliente(String pedIdentificadorCliente) {
        this.pedIdentificadorCliente = pedIdentificadorCliente;
    }
}
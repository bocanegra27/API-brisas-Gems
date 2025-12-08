package com.example.libreria_api.dto.gestionpedidos;

import java.util.Date;

public class PedidoResponseDTO {

    private Integer pedId;
    private String pedCodigo;
    private Date pedFechaCreacion;
    private String pedComentarios;
    private Integer estId;
    private String estadoNombre;
    private String renderPath;
    private Integer perId;
    private Integer usuId;

    // NUEVOS CAMPOS para sesiones anónimas
    private Integer sesionId;
    private String sesionToken;
    private Integer conId;
    private String pedIdentificadorCliente;

    // Getters y Setters existentes
    public Integer getPedId() {
        return pedId;
    }

    public void setPedId(Integer pedId) {
        this.pedId = pedId;
    }

    public String getPedCodigo() {
        return pedCodigo;
    }

    public void setPedCodigo(String pedCodigo) {
        this.pedCodigo = pedCodigo;
    }

    public Date getPedFechaCreacion() {
        return pedFechaCreacion;
    }

    public void setPedFechaCreacion(Date pedFechaCreacion) {
        this.pedFechaCreacion = pedFechaCreacion;
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

    public String getEstadoNombre() {
        return estadoNombre;
    }

    public void setEstadoNombre(String estadoNombre) {
        this.estadoNombre = estadoNombre;
    }

    public String getRenderPath() {
        return renderPath;
    }

    public void setRenderPath(String renderPath) {
        this.renderPath = renderPath;
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

    public String getSesionToken() {
        return sesionToken;
    }

    public void setSesionToken(String sesionToken) {
        this.sesionToken = sesionToken;
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
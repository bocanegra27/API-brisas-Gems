package com.example.libreria_api.dto.gestionpedidos;

import java.util.Date;

public class PedidoDetailResponseDTO {

    private int ped_id;
    private String pedCodigo;
    private Date pedFechaCreacion;
    private String pedComentarios;
    private Integer estId;
    private String estadoNombre;
    private String clienteNombre;
    private String empleadoNombre;
    private Integer perId;
    private Integer usuId;


    private Integer sesionId;
    private String sesionToken;
    private Integer conId;
    private String pedIdentificadorCliente;


    public PedidoDetailResponseDTO() {
    }


    public int getPed_id() {
        return ped_id;
    }

    public void setPed_id(int ped_id) {
        this.ped_id = ped_id;
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

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public String getEmpleadoNombre() {
        return empleadoNombre;
    }

    public void setEmpleadoNombre(String empleadoNombre) {
        this.empleadoNombre = empleadoNombre;
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

    @Override
    public String toString() {
        return "PedidoDetailResponseDTO{" +
                "ped_id=" + ped_id +
                ", pedCodigo='" + pedCodigo + '\'' +
                ", pedFechaCreacion=" + pedFechaCreacion +
                ", estId=" + estId +
                ", estadoNombre='" + estadoNombre + '\'' +
                ", clienteNombre='" + clienteNombre + '\'' +
                ", empleadoNombre='" + empleadoNombre + '\'' +
                ", perId=" + perId +
                ", usuId=" + usuId +
                ", sesionId=" + sesionId +
                ", pedIdentificadorCliente='" + pedIdentificadorCliente + '\'' +
                '}';
    }
}
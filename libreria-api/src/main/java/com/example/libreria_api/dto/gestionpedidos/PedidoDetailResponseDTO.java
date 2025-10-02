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
    private Integer perId;      // ✅ NUEVO: ID de personalización
    private Integer usuId;      // ✅ NUEVO: ID de usuario

    // Constructores
    public PedidoDetailResponseDTO() {
    }

    public PedidoDetailResponseDTO(int ped_id, String pedCodigo, Date pedFechaCreacion,
                                   String pedComentarios, Integer estId, String estadoNombre,
                                   String clienteNombre, String empleadoNombre, Integer perId, Integer usuId) {
        this.ped_id = ped_id;
        this.pedCodigo = pedCodigo;
        this.pedFechaCreacion = pedFechaCreacion;
        this.pedComentarios = pedComentarios;
        this.estId = estId;
        this.estadoNombre = estadoNombre;
        this.clienteNombre = clienteNombre;
        this.empleadoNombre = empleadoNombre;
        this.perId = perId;     // ✅ NUEVO
        this.usuId = usuId;     // ✅ NUEVO
    }

    // Getters y Setters
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

    // ✅ NUEVOS GETTERS Y SETTERS
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
                '}';
    }
}
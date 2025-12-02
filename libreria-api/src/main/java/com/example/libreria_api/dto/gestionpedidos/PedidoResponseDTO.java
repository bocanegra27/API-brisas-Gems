package com.example.libreria_api.dto.gestionpedidos;

import java.util.Date;
// Si tu modelo Pedido usa java.util.Date, mantenemos Date aquí.
// Si usas Instant o LocalDateTime en el futuro, recuerda cambiar este import.

public class PedidoResponseDTO {

    private Integer pedId;
    private String pedCodigo;
    private Date pedFechaCreacion;
    private String pedComentarios;

    private Integer estId;
    private String estadoNombre;

    // 🟢 CAMBIO CLAVE: Campo para la ruta del render (uploads/renders/...)
    private String renderPath;

    private Integer perId;
    private Integer usuId;

    // Getters y Setters

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

    // 🟢 NUEVOS GETTER Y SETTER para la ruta del render
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
}
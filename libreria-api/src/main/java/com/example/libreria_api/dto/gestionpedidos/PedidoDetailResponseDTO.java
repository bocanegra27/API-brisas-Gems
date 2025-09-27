package com.example.libreria_api.dto.gestionpedidos;

import java.util.Date;
// Asume que también tendrás DTOs para los detalles del producto, etc.
// Por ahora, lo mantenemos simple.

public class PedidoDetailResponseDTO {

    private int ped_id;
    private String pedCodigo;
    private Date pedFechaCreacion;
    private String pedComentarios;

    // Campos con más detalle
    private String estadoNombre;
    private String clienteNombre;
    private String empleadoNombre;

    // Getters y Setters
    public int getPed_id() { return ped_id; }
    public void setPed_id(int ped_id) { this.ped_id = ped_id; }

    public String getPedCodigo() { return pedCodigo; }
    public void setPedCodigo(String pedCodigo) { this.pedCodigo = pedCodigo; }

    public Date getPedFechaCreacion() { return pedFechaCreacion; }
    public void setPedFechaCreacion(Date pedFechaCreacion) { this.pedFechaCreacion = pedFechaCreacion; }

    public String getPedComentarios() { return pedComentarios; }
    public void setPedComentarios(String pedComentarios) { this.pedComentarios = pedComentarios; }

    public String getEstadoNombre() { return estadoNombre; }
    public void setEstadoNombre(String estadoNombre) { this.estadoNombre = estadoNombre; }

    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }

    public String getEmpleadoNombre() { return empleadoNombre; }
    public void setEmpleadoNombre(String empleadoNombre) { this.empleadoNombre = empleadoNombre; }
}
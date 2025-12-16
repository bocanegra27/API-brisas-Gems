package com.example.libreria_api.dto.gestionpedidos;



public class PedidoRequestDTO {


    private String pedComentarios;

    private Integer estId;
    private Integer perId;

    // Trazabilidad y Asignación
    private Integer usuIdEmpleado;
    private Integer usuIdCliente;
    private Integer sesionId;
    private Integer conId;
    private String pedIdentificadorCliente;


    public PedidoRequestDTO() {}



    public String getPedComentarios() { return pedComentarios; }
    public void setPedComentarios(String pedComentarios) { this.pedComentarios = pedComentarios; }

    public Integer getEstId() { return estId; }
    public void setEstId(Integer estId) { this.estId = estId; }

    public Integer getPerId() { return perId; }
    public void setPerId(Integer perId) { this.perId = perId; }

    public Integer getUsuIdEmpleado() { return usuIdEmpleado; }
    public void setUsuIdEmpleado(Integer usuIdEmpleado) { this.usuIdEmpleado = usuIdEmpleado; }

    public Integer getUsuIdCliente() { return usuIdCliente; }
    public void setUsuIdCliente(Integer usuIdCliente) { this.usuIdCliente = usuIdCliente; }

    public Integer getSesionId() { return sesionId; }
    public void setSesionId(Integer sesionId) { this.sesionId = sesionId; }

    public Integer getConId() { return conId; }
    public void setConId(Integer conId) { this.conId = conId; }

    public String getPedIdentificadorCliente() { return pedIdentificadorCliente; }
    public void setPedIdentificadorCliente(String pedIdentificadorCliente) { this.pedIdentificadorCliente = pedIdentificadorCliente; }


}
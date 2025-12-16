package com.example.libreria_api.dto.gestionpedidos;

public class PedidoRequestDTO {

    // Campos planos de texto
    private String pedComentarios;
    private String pedIdentificadorCliente; // Nombre/Teléfono para no registrados (anónimos)

    // IDs (Foreign Keys) para relaciones
    private Integer estId;          // Estado del pedido (opcional al crear, default=1)
    private Integer perId;          // ID de la Personalización asociada

    // Trazabilidad y Asignación de Usuarios
    private Integer usuIdEmpleado;  // ID del diseñador/admin asignado
    private Integer usuIdCliente;   // ID del cliente registrado (Se llena auto en 'crearMiPedido')
    private Integer sesionId;       // ID de la Sesión Anónima
    private Integer conId;          // ID del Contacto (si viene de formulario)

    public PedidoRequestDTO() {}



    public String getPedComentarios() { return pedComentarios; }
    public void setPedComentarios(String pedComentarios) { this.pedComentarios = pedComentarios; }

    public String getPedIdentificadorCliente() { return pedIdentificadorCliente; }
    public void setPedIdentificadorCliente(String pedIdentificadorCliente) { this.pedIdentificadorCliente = pedIdentificadorCliente; }

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
}
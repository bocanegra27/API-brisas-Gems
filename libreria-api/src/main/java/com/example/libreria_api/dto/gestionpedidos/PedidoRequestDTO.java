package com.example.libreria_api.dto.gestionpedidos;

// Usamos el mismo nombre que antes (usuId) si se usa en otros DTOs,
// pero en este contexto, lo redefinimos para ser claro.

public class PedidoRequestDTO {

    // Campos planos que pueden ser actualizados/enviados
    private String pedComentarios;

    // IDs (Foreign Keys) que vienen del cliente/frontend
    private Integer estId; // Estado al que se quiere cambiar/crear
    private Integer perId; // ID de Personalizacion

    // Trazabilidad y Asignación
    private Integer usuIdEmpleado; // ID del diseñador/admin asignado
    private Integer usuIdCliente;  // ID del cliente registrado
    private Integer sesionId;      // ID de la Sesión Anónima
    private Integer conId;         // ID del Contacto Formulario
    private String pedIdentificadorCliente; // Nombre/Teléfono para no registrados

    // Constructor vacío
    public PedidoRequestDTO() {}

    // ===================================
    // Getters y Setters
    // ===================================

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

    // NOTA: Eliminar cualquier getter/setter duplicado o antiguo como getUsuId()
}
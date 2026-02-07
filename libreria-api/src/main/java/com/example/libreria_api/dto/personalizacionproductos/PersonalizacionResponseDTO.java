package com.example.libreria_api.dto.personalizacionproductos;

import java.time.LocalDateTime;
import java.util.List;

public class PersonalizacionResponseDTO {
    private Integer id;
    private LocalDateTime fecha;
    private Integer usuarioClienteId;
    private String usuarioNombre;
    private Integer sesionId;
    private String sesionToken;
    private String tipoCliente;
    private List<DetalleDTO> detalles;
    private Integer catId;
    private String catNombre;

    public static class DetalleDTO {
        private Integer detId;
        private Integer valId;
        private String valNombre;
        private Integer opcionId;
        private String opcionNombre;

        public Integer getDetId() { return detId; }
        public void setDetId(Integer detId) { this.detId = detId; }
        public Integer getValId() { return valId; }
        public void setValId(Integer valId) { this.valId = valId; }
        public String getValNombre() { return valNombre; }
        public void setValNombre(String valNombre) { this.valNombre = valNombre; }
        public Integer getOpcionId() { return opcionId; }
        public void setOpcionId(Integer opcionId) { this.opcionId = opcionId; }
        public String getOpcionNombre() { return opcionNombre; }
        public void setOpcionNombre(String opcionNombre) { this.opcionNombre = opcionNombre; }
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public Integer getUsuarioClienteId() { return usuarioClienteId; }
    public void setUsuarioClienteId(Integer usuarioClienteId) { this.usuarioClienteId = usuarioClienteId; }
    public String getUsuarioNombre() { return usuarioNombre; }
    public void setUsuarioNombre(String usuarioNombre) { this.usuarioNombre = usuarioNombre; }
    public Integer getSesionId() { return sesionId; }
    public void setSesionId(Integer sesionId) { this.sesionId = sesionId; }
    public String getSesionToken() { return sesionToken; }
    public void setSesionToken(String sesionToken) { this.sesionToken = sesionToken; }
    public String getTipoCliente() { return tipoCliente; }
    public void setTipoCliente(String tipoCliente) { this.tipoCliente = tipoCliente; }
    public List<DetalleDTO> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleDTO> detalles) { this.detalles = detalles; }
    public Integer getCatId() { return catId; }
    public void setCatId(Integer catId) { this.catId = catId; }
    public String getCatNombre() { return catNombre; }
    public void setCatNombre(String catNombre) { this.catNombre = catNombre; }
}
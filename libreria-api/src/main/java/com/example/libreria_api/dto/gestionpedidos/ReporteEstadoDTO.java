package com.example.libreria_api.dto.gestionpedidos;

public class ReporteEstadoDTO {

    private Integer estId;
    private String estNombre;
    private Long total;

    public ReporteEstadoDTO(Integer estId, String estNombre, Long total) {
        this.estId = estId;
        this.estNombre = estNombre;
        this.total = total;
    }

    public Integer getEstId() { return estId; }
    public void setEstId(Integer estId) { this.estId = estId; }

    public String getEstNombre() { return estNombre; }
    public void setEstNombre(String estNombre) { this.estNombre = estNombre; }

    public Long getTotal() { return total; }
    public void setTotal(Long total) { this.total = total; }
}
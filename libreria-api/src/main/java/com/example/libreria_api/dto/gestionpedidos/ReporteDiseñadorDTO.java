package com.example.libreria_api.dto.gestionpedidos;

public class ReporteDiseñadorDTO {

    private Integer usuId;
    private String usuNombre;
    private Long totalAsignados;

    public ReporteDiseñadorDTO(Integer usuId, String usuNombre, Long totalAsignados) {
        this.usuId = usuId;
        this.usuNombre = usuNombre;
        this.totalAsignados = totalAsignados;
    }

    public Integer getUsuId() { return usuId; }
    public void setUsuId(Integer usuId) { this.usuId = usuId; }

    public String getUsuNombre() { return usuNombre; }
    public void setUsuNombre(String usuNombre) { this.usuNombre = usuNombre; }

    public Long getTotalAsignados() { return totalAsignados; }
    public void setTotalAsignados(Long totalAsignados) { this.totalAsignados = totalAsignados; }
}
package com.example.libreria_api.dto.gestionpedidos;

import java.util.Date;

public class HistorialResponseDTO {

    private Integer hisId;
    private Date hisFechaCambio;
    private String hisComentarios;
    private String hisImagen;


    private Integer estId;
    private String estadoNombre;


    private Integer responsableId;
    private String responsableNombre;

    public HistorialResponseDTO() {}


    public Integer getHisId() {
        return hisId;
    }

    public Date getHisFechaCambio() {
        return hisFechaCambio;
    }

    public String getHisComentarios() {
        return hisComentarios;
    }

    public String getHisImagen() {
        return hisImagen;
    }

    public Integer getEstId() {
        return estId;
    }

    public String getEstadoNombre() {
        return estadoNombre;
    }

    public Integer getResponsableId() {
        return responsableId;
    }

    public String getResponsableNombre() {
        return responsableNombre;
    }


    public void setHisId(Integer hisId) {
        this.hisId = hisId;
    }

    public void setHisFechaCambio(Date hisFechaCambio) {
        this.hisFechaCambio = hisFechaCambio;
    }

    public void setHisComentarios(String hisComentarios) {
        this.hisComentarios = hisComentarios;
    }

    public void setHisImagen(String hisImagen) {
        this.hisImagen = hisImagen;
    }

    public void setEstId(Integer estId) {
        this.estId = estId;
    }

    public void setEstadoNombre(String estadoNombre) {
        this.estadoNombre = estadoNombre;
    }

    public void setResponsableId(Integer responsableId) {
        this.responsableId = responsableId;
    }

    public void setResponsableNombre(String responsableNombre) {
        this.responsableNombre = responsableNombre;
    }
}
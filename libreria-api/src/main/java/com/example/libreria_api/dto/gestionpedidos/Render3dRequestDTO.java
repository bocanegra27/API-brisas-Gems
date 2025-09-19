package com.example.libreria_api.dto.gestionpedidos;

import java.time.LocalDate;

public class Render3dRequestDTO {

    private String renImagen;
    private LocalDate renFechaAprobacion;
    private Integer ped_id;

     public String getRenImagen() {
        return renImagen;
    }

    public void setRenImagen(String renImagen) {
        this.renImagen = renImagen;
    }

    public LocalDate getRenFechaAprobacion() {
        return renFechaAprobacion;
    }

    public void setRenFechaAprobacion(LocalDate renFechaAprobacion) {
        this.renFechaAprobacion = renFechaAprobacion;
    }

    public Integer getPed_id() {
        return ped_id;
    }

    public void setPed_id(Integer ped_id) {
        this.ped_id = ped_id;
    }
}
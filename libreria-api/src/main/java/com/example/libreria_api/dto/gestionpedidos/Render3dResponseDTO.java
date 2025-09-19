package com.example.libreria_api.dto.gestionpedidos;

import java.time.LocalDate;

public class Render3dResponseDTO {

    private int ren_id;
    private String renImagen;
    private LocalDate renFechaAprobacion;
    private Integer ped_id;

    // Getters y Setters
    public int getRen_id() {
        return ren_id;
    }

    public void setRen_id(int ren_id) {
        this.ren_id = ren_id;
    }

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
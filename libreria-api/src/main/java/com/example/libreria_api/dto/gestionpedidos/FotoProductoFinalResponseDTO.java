package com.example.libreria_api.dto.gestionpedidos;

import java.time.LocalDate;

public class FotoProductoFinalResponseDTO {

    private int fot_id;
    private String fotImagenFinal;
    private LocalDate fotFechaSubida;
    private Integer ped_id;


    public int getFot_id() {
        return fot_id;
    }

    public void setFot_id(int fot_id) {
        this.fot_id = fot_id;
    }

    public String getFotImagenFinal() {
        return fotImagenFinal;
    }

    public void setFotImagenFinal(String fotImagenFinal) {
        this.fotImagenFinal = fotImagenFinal;
    }

    public LocalDate getFotFechaSubida() {
        return fotFechaSubida;
    }

    public void setFotFechaSubida(LocalDate fotFechaSubida) {
        this.fotFechaSubida = fotFechaSubida;
    }

    public Integer getPed_id() {
        return ped_id;
    }

    public void setPed_id(Integer ped_id) {
        this.ped_id = ped_id;
    }
}
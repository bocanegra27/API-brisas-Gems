package com.example.libreria_api.dto.gestionpedidos;

public class FotoProductoFinalRequestDTO {

    private String fotImagenFinal;
    private Integer ped_id;


    public String getFotImagenFinal() {
        return fotImagenFinal;
    }

    public void setFotImagenFinal(String fotImagenFinal) {
        this.fotImagenFinal = fotImagenFinal;
    }

    public Integer getPed_id() {
        return ped_id;
    }

    public void setPed_id(Integer ped_id) {
        this.ped_id = ped_id;
    }
}
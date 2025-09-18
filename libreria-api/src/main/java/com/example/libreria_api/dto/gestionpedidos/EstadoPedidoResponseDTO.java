package com.example.libreria_api.dto.gestionpedidos;

public class EstadoPedidoResponseDTO {

    private int est_id;
    private String estNombre;

    public int getEst_id() {
        return est_id;
    }

    public void setEst_id(int est_id) {
        this.est_id = est_id;
    }

    public String getEstNombre() {
        return estNombre;
    }

    public void setEstNombre(String estNombre) {
        this.estNombre = estNombre;
    }
}
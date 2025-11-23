package com.example.libreria_api.dto.gestionpedidos;

public class EstadoPedidoResponseDTO {

    private int est_id;
    private String estNombre;
    private String estDescripcion;

    // Constructores
    public EstadoPedidoResponseDTO() {
    }

    public EstadoPedidoResponseDTO(int est_id, String estNombre, String estDescripcion) {
        this.est_id = est_id;
        this.estNombre = estNombre;
        this.estDescripcion = estDescripcion;
    }

    // Getters y Setters
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

    public String getEstDescripcion() {
        return estDescripcion;
    }

    public void setEstDescripcion(String estDescripcion) {
        this.estDescripcion = estDescripcion;
    }

    @Override
    public String toString() {
        return "EstadoPedidoResponseDTO{" +
                "est_id=" + est_id +
                ", estNombre='" + estNombre + '\'' +
                ", estDescripcion='" + estDescripcion + '\'' +
                '}';
    }
}
package com.example.libreria_api.dto.gestionpedidos;

public class EstadoPedidoRequestDTO {

    private String estNombre;
    private String estDescripcion;


    public EstadoPedidoRequestDTO() {
    }

    public EstadoPedidoRequestDTO(String estNombre, String estDescripcion) {
        this.estNombre = estNombre;
        this.estDescripcion = estDescripcion;
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
}
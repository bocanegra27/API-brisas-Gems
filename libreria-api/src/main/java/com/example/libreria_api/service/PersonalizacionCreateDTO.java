package com.example.libreria_api.service;

import java.time.LocalDate;
import java.util.List;

public class PersonalizacionCreateDTO {

    private LocalDate fecha;
    private int usuarioClienteId;
    private List<Integer> valoresSeleccionados; // IDs de valor_personalizacion

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public int getUsuarioClienteId() {
        return usuarioClienteId;
    }

    public void setUsuarioClienteId(int usuarioClienteId) {
        this.usuarioClienteId = usuarioClienteId;
    }

    public List<Integer> getValoresSeleccionados() {
        return valoresSeleccionados;
    }

    public void setValoresSeleccionados(List<Integer> valoresSeleccionados) {
        this.valoresSeleccionados = valoresSeleccionados;
    }
}

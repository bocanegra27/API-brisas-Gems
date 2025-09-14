package com.example.libreria_api.service;

import java.time.LocalDate;
import java.util.List;

public class PersonalizacionUpdateDTO {

    private LocalDate fecha;
    private Integer usuarioClienteId;
    private List<Integer> valoresSeleccionados;

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Integer getUsuarioClienteId() {
        return usuarioClienteId;
    }

    public void setUsuarioClienteId(Integer usuarioClienteId) {
        this.usuarioClienteId = usuarioClienteId;
    }

    public List<Integer> getValoresSeleccionados() {
        return valoresSeleccionados;
    }

    public void setValoresSeleccionados(List<Integer> valoresSeleccionados) {
        this.valoresSeleccionados = valoresSeleccionados;
    }
}


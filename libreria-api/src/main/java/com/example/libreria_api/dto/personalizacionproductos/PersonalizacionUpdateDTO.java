package com.example.libreria_api.dto.personalizacionproductos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PersonalizacionUpdateDTO {

    private LocalDateTime fecha;
    private Integer usuarioClienteId;
    private List<Integer> valoresSeleccionados;

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
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


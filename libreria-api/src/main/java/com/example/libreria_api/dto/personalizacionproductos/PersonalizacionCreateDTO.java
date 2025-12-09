package com.example.libreria_api.dto.personalizacionproductos;

import java.time.LocalDateTime;
import java.util.List;

public class PersonalizacionCreateDTO {
    private LocalDateTime fecha;
    private Integer usuarioClienteId;
    private Integer sesionId;
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

    public Integer getSesionId() {
        return sesionId;
    }

    public void setSesionId(Integer sesionId) {
        this.sesionId = sesionId;
    }

    public List<Integer> getValoresSeleccionados() {
        return valoresSeleccionados;
    }

    public void setValoresSeleccionados(List<Integer> valoresSeleccionados) {
        this.valoresSeleccionados = valoresSeleccionados;
    }
}
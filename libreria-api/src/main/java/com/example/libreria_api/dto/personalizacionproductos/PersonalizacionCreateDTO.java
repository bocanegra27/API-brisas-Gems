package com.example.libreria_api.dto.personalizacionproductos;


import java.time.LocalDate;
import java.util.List;

public class PersonalizacionCreateDTO {

    private LocalDate fecha;
    private Integer usuarioClienteId; // CAMBIAR de int a Integer (permite null)
    private List<Integer> valoresSeleccionados;

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Integer getUsuarioClienteId() { // CAMBIAR tipo de retorno
        return usuarioClienteId;
    }

    public void setUsuarioClienteId(Integer usuarioClienteId) { // CAMBIAR tipo de parámetro
        this.usuarioClienteId = usuarioClienteId;
    }

    public List<Integer> getValoresSeleccionados() {
        return valoresSeleccionados;
    }

    public void setValoresSeleccionados(List<Integer> valoresSeleccionados) {
        this.valoresSeleccionados = valoresSeleccionados;
    }
}
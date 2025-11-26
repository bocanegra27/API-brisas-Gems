package com.example.libreria_api.dto.personalizacionproductos;

import java.time.LocalDate;
import java.util.List;

public class PersonalizacionResponseDTO {

    private int id;
    private LocalDate fecha;
    private Integer usuarioClienteId; // ✅ CAMBIO: int → Integer (permite null)
    private String usuarioNombre;
    private List<DetalleDTO> detalles;

    // Clase interna para los detalles
    public static class DetalleDTO {
        private int detId;
        private int valId;
        private String valNombre;
        private int opcionId;
        private String opcionNombre;

        // Getters y setters
        public int getDetId() { return detId; }
        public void setDetId(int detId) { this.detId = detId; }

        public int getValId() { return valId; }
        public void setValId(int valId) { this.valId = valId; }

        public String getValNombre() { return valNombre; }
        public void setValNombre(String valNombre) { this.valNombre = valNombre; }

        public int getOpcionId() { return opcionId; }
        public void setOpcionId(int opcionId) { this.opcionId = opcionId; }

        public String getOpcionNombre() { return opcionNombre; }
        public void setOpcionNombre(String opcionNombre) { this.opcionNombre = opcionNombre; }
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public Integer getUsuarioClienteId() { return usuarioClienteId; } // ✅ CAMBIO
    public void setUsuarioClienteId(Integer usuarioClienteId) { this.usuarioClienteId = usuarioClienteId; } // ✅ CAMBIO

    public String getUsuarioNombre() { return usuarioNombre; }
    public void setUsuarioNombre(String usuarioNombre) { this.usuarioNombre = usuarioNombre; }

    public List<DetalleDTO> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleDTO> detalles) { this.detalles = detalles; }
}
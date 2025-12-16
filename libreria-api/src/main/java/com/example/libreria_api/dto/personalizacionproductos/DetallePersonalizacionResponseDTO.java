package com.example.libreria_api.dto.personalizacionproductos;

public class DetallePersonalizacionResponseDTO {
    private Integer detId;
    private Integer perId;
    private Integer valId;
    private String valNombre;
    private Integer opcionId;
    private String opcionNombre;


    public Integer getDetId() {
        return detId;
    }
    public void setDetId(Integer detId) {
        this.detId = detId;
    }

    public Integer getPerId() {
        return perId;
    }
    public void setPerId(Integer perId) {
        this.perId = perId;
    }

    public Integer getValId() {
        return valId;
    }
    public void setValId(Integer valId) {
        this.valId = valId;
    }

    public String getValNombre() {
        return valNombre;
    }
    public void setValNombre(String valNombre) {
        this.valNombre = valNombre;
    }

    public Integer getOpcionId() {
        return opcionId;
    }
    public void setOpcionId(Integer opcionId) {
        this.opcionId = opcionId;
    }

    public String getOpcionNombre() {
        return opcionNombre;
    }
    public void setOpcionNombre(String opcionNombre) {
        this.opcionNombre = opcionNombre;
    }
}

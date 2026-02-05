package com.example.libreria_api.dto.sistemausuarios;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class UsuarioUpdateDTO {

    @Size(max = 150)
    private String nombre;

    @Email
    @Size(max = 100)
    private String correo;

    @Size(max = 20)
    private String telefono;

    @Size(max = 20)
    private String docnum;

    private Integer rolId;

    private Integer tipdocId;

    private Boolean activo;

    private String origen;

    public UsuarioUpdateDTO() {
    }


    public UsuarioUpdateDTO(String nombre, String correo, String telefono, String docnum,
                            Integer rolId, Integer tipdocId, Boolean activo, String origen) {
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.docnum = docnum;
        this.rolId = rolId;
        this.tipdocId = tipdocId;
        this.activo = activo;
        this.origen = origen;
    }


    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDocnum() { return docnum; }
    public void setDocnum(String docnum) { this.docnum = docnum; }

    public Integer getRolId() { return rolId; }
    public void setRolId(Integer rolId) { this.rolId = rolId; }

    public Integer getTipdocId() { return tipdocId; }
    public void setTipdocId(Integer tipdocId) { this.tipdocId = tipdocId; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }
}

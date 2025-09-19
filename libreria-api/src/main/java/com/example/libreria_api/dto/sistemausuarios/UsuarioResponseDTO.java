package com.example.libreria_api.dto.sistemausuarios;

import com.example.libreria_api.model.sistemausuarios.OrigenUsuario;

public class UsuarioResponseDTO {

    private int usu_id;
    private String usuNombre;
    private String usuCorreo;
    private String usuTelefono;
    private String usuDocnum;
    private OrigenUsuario usuOrigen;
    private boolean usuActivo;
    private Integer tipoDeDocumentoId;
    private Integer rolId;

    // Getters y Setters
    public int getUsu_id() {
        return usu_id;
    }

    public void setUsu_id(int usu_id) {
        this.usu_id = usu_id;
    }

    public String getUsuNombre() {
        return usuNombre;
    }

    public void setUsuNombre(String usuNombre) {
        this.usuNombre = usuNombre;
    }

    public String getUsuCorreo() {
        return usuCorreo;
    }

    public void setUsuCorreo(String usuCorreo) {
        this.usuCorreo = usuCorreo;
    }

    public String getUsuTelefono() {
        return usuTelefono;
    }

    public void setUsuTelefono(String usuTelefono) {
        this.usuTelefono = usuTelefono;
    }

    public String getUsuDocnum() {
        return usuDocnum;
    }

    public void setUsuDocnum(String usuDocnum) {
        this.usuDocnum = usuDocnum;
    }

    public OrigenUsuario getUsuOrigen() {
        return usuOrigen;
    }

    public void setUsuOrigen(OrigenUsuario usuOrigen) {
        this.usuOrigen = usuOrigen;
    }

    public boolean isUsuActivo() {
        return usuActivo;
    }

    public void setUsuActivo(boolean usuActivo) {
        this.usuActivo = usuActivo;
    }

    public Integer getTipoDeDocumentoId() {
        return tipoDeDocumentoId;
    }

    public void setTipoDeDocumentoId(Integer tipoDeDocumentoId) {
        this.tipoDeDocumentoId = tipoDeDocumentoId;
    }

    public Integer getRolId() {
        return rolId;
    }

    public void setRolId(Integer rolId) {
        this.rolId = rolId;
    }
}

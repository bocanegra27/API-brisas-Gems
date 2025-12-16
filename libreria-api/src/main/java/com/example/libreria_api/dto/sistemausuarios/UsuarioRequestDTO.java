package com.example.libreria_api.dto.sistemausuarios;

public class UsuarioRequestDTO {

    private String usuNombre;
    private String usuCorreo;
    private String usuTelefono;
    private String usuPassword;
    private String usuDocnum;
    private Integer tipoDeDocumentoId;


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

    public String getUsuPassword() {
        return usuPassword;
    }

    public void setUsuPassword(String usuPassword) {
        this.usuPassword = usuPassword;
    }

    public String getUsuDocnum() {
        return usuDocnum;
    }

    public void setUsuDocnum(String usuDocnum) {
        this.usuDocnum = usuDocnum;
    }

    public Integer getTipoDeDocumentoId() {
        return tipoDeDocumentoId;
    }

    public void setTipoDeDocumentoId(Integer tipoDeDocumentoId) {
        this.tipoDeDocumentoId = tipoDeDocumentoId;
    }
}

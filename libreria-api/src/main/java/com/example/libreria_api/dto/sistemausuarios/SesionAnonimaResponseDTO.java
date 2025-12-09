package com.example.libreria_api.dto.sistemausuarios;

import java.time.LocalDateTime;

public class SesionAnonimaResponseDTO {
    private Integer sesId;
    private String sesToken;
    private LocalDateTime sesFechaCreacion;
    private LocalDateTime sesFechaExpiracion;
    private Boolean sesConvertido;
    private Integer usuarioConvertidoId;

    public Integer getSesId() {
        return sesId;
    }

    public void setSesId(Integer sesId) {
        this.sesId = sesId;
    }

    public String getSesToken() {
        return sesToken;
    }

    public void setSesToken(String sesToken) {
        this.sesToken = sesToken;
    }

    public LocalDateTime getSesFechaCreacion() {
        return sesFechaCreacion;
    }

    public void setSesFechaCreacion(LocalDateTime sesFechaCreacion) {
        this.sesFechaCreacion = sesFechaCreacion;
    }

    public LocalDateTime getSesFechaExpiracion() {
        return sesFechaExpiracion;
    }

    public void setSesFechaExpiracion(LocalDateTime sesFechaExpiracion) {
        this.sesFechaExpiracion = sesFechaExpiracion;
    }

    public Boolean getSesConvertido() {
        return sesConvertido;
    }

    public void setSesConvertido(Boolean sesConvertido) {
        this.sesConvertido = sesConvertido;
    }

    public Integer getUsuarioConvertidoId() {
        return usuarioConvertidoId;
    }

    public void setUsuarioConvertidoId(Integer usuarioConvertidoId) {
        this.usuarioConvertidoId = usuarioConvertidoId;
    }
}
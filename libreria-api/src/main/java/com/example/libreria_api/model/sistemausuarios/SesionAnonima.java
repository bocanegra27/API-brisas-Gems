package com.example.libreria_api.model.sistemausuarios;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sesion_anonima")
public class SesionAnonima {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ses_id")
    private Integer sesId;

    @Column(name = "ses_token", unique = true, nullable = false, length = 100)
    private String sesToken;

    @Column(name = "ses_fecha_creacion", nullable = false)
    private LocalDateTime sesFechaCreacion;

    @Column(name = "ses_fecha_expiracion", nullable = false)
    private LocalDateTime sesFechaExpiracion;

    @Column(name = "ses_convertido", nullable = false)
    private Boolean sesConvertido = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usu_id_convertido", referencedColumnName = "usu_id")
    private Usuario usuarioConvertido;

    // Constructores
    public SesionAnonima() {
    }

    public SesionAnonima(String sesToken) {
        this.sesToken = sesToken;
        this.sesFechaCreacion = LocalDateTime.now();
        this.sesFechaExpiracion = LocalDateTime.now().plusDays(30);
        this.sesConvertido = false;
    }

    // Getters y Setters
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

    public Usuario getUsuarioConvertido() {
        return usuarioConvertido;
    }

    public void setUsuarioConvertido(Usuario usuarioConvertido) {
        this.usuarioConvertido = usuarioConvertido;
    }

    // Metodo helper para verificar si esta expirada
    public boolean isExpirada() {
        return LocalDateTime.now().isAfter(this.sesFechaExpiracion);
    }
}
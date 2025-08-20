package com.example.libreria_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int usu_id;

    @Column(name = "usu_nombre")
    private String usuNombre;

    @Column(name = "usu_correo")
    private String usuCorreo;

    @Column(name = "usu_telefono")
    private String usuTelefono;

    @Column(name = "usu_password")
    private String usuPassword;

    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Rol rol;

    public Usuario() {
    }

    public Usuario(String usuNombre, String usuCorreo, String usuTelefono, String usuPassword, Rol rol) {
        this.usuNombre = usuNombre;
        this.usuCorreo = usuCorreo;
        this.usuTelefono = usuTelefono;
        this.usuPassword = usuPassword;
        this.rol = rol;
    }



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

    public String getUsuPassword() {
        return usuPassword;
    }

    public void setUsuPassword(String usuPassword) {
        this.usuPassword = usuPassword;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}
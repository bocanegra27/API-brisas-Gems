package com.example.libreria_api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contacto_formulario")
public class ContactoFormulario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int con_id;

    @Column(name = "con_nombre", length = 150, nullable = false)
    private String conNombre;

    @Column(name = "con_email", length = 100)
    private String conEmail;

    @Column(name = "con_telefono", length = 30)
    private String conTelefono;

    @Column(name = "con_mensaje", columnDefinition = "TEXT", nullable = false)
    private String conMensaje;

    @Column(name = "con_fecha_envio", nullable = false)
    private LocalDateTime conFechaEnvio;

    @Column(name = "con_via", columnDefinition = "ENUM('formulario','whatsapp')")
    private String conVia = "formulario";

    @Column(name = "con_terminos", nullable = false)
    private boolean conTerminos;

    @ManyToOne
    @JoinColumn(name = "usu_id", referencedColumnName = "usu_id")
    private Usuario usuario; // relación con usuarios


    public ContactoFormulario() {
    }


    public ContactoFormulario(String conNombre, String conEmail, String conTelefono,
                              String conMensaje, boolean conTerminos, Usuario usuario) {
        this.conNombre = conNombre;
        this.conEmail = conEmail;
        this.conTelefono = conTelefono;
        this.conMensaje = conMensaje;
        this.conFechaEnvio = LocalDateTime.now(); // valor por defecto
        this.conTerminos = conTerminos;
        this.usuario = usuario;
    }

    @PrePersist
    protected void onCreate() {
        this.conFechaEnvio = LocalDateTime.now();
    }

    // Getters y Setters
    public int getCon_id() {
        return con_id;
    }

    public void setCon_id(int con_id) {
        this.con_id = con_id;
    }

    public String getConNombre() {
        return conNombre;
    }

    public void setConNombre(String conNombre) {
        this.conNombre = conNombre;
    }

    public String getConEmail() {
        return conEmail;
    }

    public void setConEmail(String conEmail) {
        this.conEmail = conEmail;
    }

    public String getConTelefono() {
        return conTelefono;
    }

    public void setConTelefono(String conTelefono) {
        this.conTelefono = conTelefono;
    }

    public String getConMensaje() {
        return conMensaje;
    }

    public void setConMensaje(String conMensaje) {
        this.conMensaje = conMensaje;
    }

    public LocalDateTime getConFechaEnvio() {
        return conFechaEnvio;
    }

    public void setConFechaEnvio(LocalDateTime conFechaEnvio) {
        this.conFechaEnvio = conFechaEnvio;
    }

    public String getConVia() {
        return conVia;
    }

    public void setConVia(String conVia) {
        this.conVia = conVia;
    }

    public boolean isConTerminos() {
        return conTerminos;
    }

    public void setConTerminos(boolean conTerminos) {
        this.conTerminos = conTerminos;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
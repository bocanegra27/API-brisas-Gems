package com.example.libreria_api.model.sistemausuarios;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int usu_id;

    @Column(name = "usu_nombre", nullable = false, length = 150)
    private String usuNombre;

    @Column(name = "usu_correo", nullable = false, unique = true, length = 100)
    private String usuCorreo;

    @Column(name = "usu_telefono", length = 20)
    private String usuTelefono;

    @Column(name = "usu_password", nullable = false, length = 255)
    private String usuPassword;

    @Column(name = "usu_docnum", length = 20)
    private String usuDocnum;

    @Enumerated(EnumType.STRING)
    @Column(name = "usu_origen", nullable = false)
    private OrigenUsuario usuOrigen;

    @Column(name = "usu_activo", nullable = false)
    private boolean usuActivo;

    @ManyToOne
    @JoinColumn(name = "tipdoc_id")
    private TipoDeDocumento tipoDeDocumento;

    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Rol rol;

    // --- Constructores ---
    public Usuario() {
    }

    // Constructor actualizado con todos los campos
    public Usuario(String usuNombre, String usuCorreo, String usuTelefono, String usuPassword, String usuDocnum, OrigenUsuario usuOrigen, boolean usuActivo, TipoDeDocumento tipoDeDocumento, Rol rol) {
        this.usuNombre = usuNombre;
        this.usuCorreo = usuCorreo;
        this.usuTelefono = usuTelefono;
        this.usuPassword = usuPassword;
        this.usuDocnum = usuDocnum;
        this.usuOrigen = usuOrigen;
        this.usuActivo = usuActivo;
        this.tipoDeDocumento = tipoDeDocumento;
        this.rol = rol;
    }

    // --- Getters y Setters para TODOS los campos ---

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

    public TipoDeDocumento getTipoDeDocumento() {
        return tipoDeDocumento;
    }

    public void setTipoDeDocumento(TipoDeDocumento tipoDeDocumento) {
        this.tipoDeDocumento = tipoDeDocumento;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}

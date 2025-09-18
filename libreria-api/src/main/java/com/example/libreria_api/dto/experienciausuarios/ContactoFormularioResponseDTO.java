package com.example.libreria_api.dto.experienciausuarios;

import java.time.LocalDateTime;

public class ContactoFormularioResponseDTO {

    private Integer id;
    private String nombre;
    private String correo;
    private String telefono;
    private String mensaje;
    private LocalDateTime fechaEnvio;
    private String via;
    private boolean terminos;
    private String estado;
    private String notas;

    private Integer usuarioId;
    private String usuarioNombre;

    private Integer usuarioIdAdmin;
    private String usuarioAdminNombre;

    // ===== Constructores =====
    public ContactoFormularioResponseDTO() {}

    public ContactoFormularioResponseDTO(Integer id, String nombre, String correo, String telefono, String mensaje,
                                         LocalDateTime fechaEnvio, String via, boolean terminos,
                                         String estado, String notas,
                                         Integer usuarioId, String usuarioNombre,
                                         Integer usuarioIdAdmin, String usuarioAdminNombre) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.mensaje = mensaje;
        this.fechaEnvio = fechaEnvio;
        this.via = via;
        this.terminos = terminos;
        this.estado = estado;
        this.notas = notas;
        this.usuarioId = usuarioId;
        this.usuarioNombre = usuarioNombre;
        this.usuarioIdAdmin = usuarioIdAdmin;
        this.usuarioAdminNombre = usuarioAdminNombre;
    }

    // ===== Getters y Setters =====
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(LocalDateTime fechaEnvio) { this.fechaEnvio = fechaEnvio; }

    public String getVia() { return via; }
    public void setVia(String via) { this.via = via; }

    public boolean isTerminos() { return terminos; }
    public void setTerminos(boolean terminos) { this.terminos = terminos; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }

    public String getUsuarioNombre() { return usuarioNombre; }
    public void setUsuarioNombre(String usuarioNombre) { this.usuarioNombre = usuarioNombre; }

    public Integer getUsuarioIdAdmin() { return usuarioIdAdmin; }
    public void setUsuarioIdAdmin(Integer usuarioIdAdmin) { this.usuarioIdAdmin = usuarioIdAdmin; }

    public String getUsuarioAdminNombre() { return usuarioAdminNombre; }
    public void setUsuarioAdminNombre(String usuarioAdminNombre) { this.usuarioAdminNombre = usuarioAdminNombre; }

    @Override
    public String toString() {
        return "ContactoFormularioResponseDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", telefono='" + telefono + '\'' +
                ", mensaje='" + mensaje + '\'' +
                ", fechaEnvio=" + fechaEnvio +
                ", via='" + via + '\'' +
                ", terminos=" + terminos +
                ", estado='" + estado + '\'' +
                ", notas='" + notas + '\'' +
                ", usuarioId=" + usuarioId +
                ", usuarioNombre='" + usuarioNombre + '\'' +
                ", usuarioIdAdmin=" + usuarioIdAdmin +
                ", usuarioAdminNombre='" + usuarioAdminNombre + '\'' +
                '}';
    }
}

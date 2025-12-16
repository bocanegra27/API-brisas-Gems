package com.example.libreria_api.dto.experienciausuarios;

import jakarta.validation.constraints.*;

public class ContactoFormularioCreateDTO {

    @NotBlank
    @Size(max = 150)
    private String nombre;

    @Email
    @Size(max = 100)
    private String correo;

    @Size(max = 30)
    private String telefono;

    @NotBlank
    private String mensaje;

    private String via;

    @AssertTrue(message = "Debe aceptar los terminos")
    private boolean terminos;

    // NUEVOS CAMPOS para sesiones anonimas
    private Integer usuarioId;
    private Integer sesionId;
    private Integer personalizacionId;

    public ContactoFormularioCreateDTO() {}


    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public String getVia() { return via; }
    public void setVia(String via) { this.via = via; }

    public boolean isTerminos() { return terminos; }
    public void setTerminos(boolean terminos) { this.terminos = terminos; }



    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }

    public Integer getSesionId() { return sesionId; }
    public void setSesionId(Integer sesionId) { this.sesionId = sesionId; }

    public Integer getPersonalizacionId() { return personalizacionId; }
    public void setPersonalizacionId(Integer personalizacionId) {
        this.personalizacionId = personalizacionId;
    }

    @Override
    public String toString() {
        return "ContactoFormularioCreateDTO{" +
                "nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", telefono='" + telefono + '\'' +
                ", mensaje='" + mensaje + '\'' +
                ", via='" + via + '\'' +
                ", terminos=" + terminos +
                ", sesionId=" + sesionId +
                ", personalizacionId=" + personalizacionId +
                '}';
    }
}
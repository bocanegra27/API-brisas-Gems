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

    private String via; // opcional, null = usa default de BD

    @AssertTrue(message = "Debe aceptar los términos")
    private boolean terminos;

    // ===== Constructores =====
    public ContactoFormularioCreateDTO() {}

    public ContactoFormularioCreateDTO(String nombre, String correo, String telefono, String mensaje, String via, boolean terminos) {
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.mensaje = mensaje;
        this.via = via;
        this.terminos = terminos;
    }

    // ===== Getters y Setters =====
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

    @Override
    public String toString() {
        return "ContactoFormularioCreateDTO{" +
                "nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", telefono='" + telefono + '\'' +
                ", mensaje='" + mensaje + '\'' +
                ", via='" + via + '\'' +
                ", terminos=" + terminos +
                '}';
    }
}

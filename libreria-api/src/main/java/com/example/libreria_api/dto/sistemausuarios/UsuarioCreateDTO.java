package com.example.libreria_api.dto.sistemausuarios;

import jakarta.validation.constraints.*;

public class UsuarioCreateDTO {

    @NotBlank
    @Size(max = 150)
    private String nombre;

    @NotBlank
    @Size(max = 100)
    @Email
    private String correo;

    @Size(max = 20)
    private String telefono;

    @NotBlank
    @Size(min = 8)
    private String password;

    @Size(max = 20)
    private String docnum;

    @NotNull
    private Integer rolId;   // obligatorio

    private Integer tipdocId; // opcional

    private String origen; // "registro", "formulario", "admin"

    private Boolean activo;

    // Constructor vacío
    public UsuarioCreateDTO() {
    }

    // Constructor completo
    public UsuarioCreateDTO(String nombre, String correo, String telefono, String password,
                            String docnum, Integer rolId, Integer tipdocId,
                            String origen, Boolean activo) {
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.password = password;
        this.docnum = docnum;
        this.rolId = rolId;
        this.tipdocId = tipdocId;
        this.origen = origen;
        this.activo = activo;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getDocnum() { return docnum; }
    public void setDocnum(String docnum) { this.docnum = docnum; }

    public Integer getRolId() { return rolId; }
    public void setRolId(Integer rolId) { this.rolId = rolId; }

    public Integer getTipdocId() { return tipdocId; }
    public void setTipdocId(Integer tipdocId) { this.tipdocId = tipdocId; }

    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}

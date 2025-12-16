package com.example.libreria_api.dto.sistemausuarios;


import com.example.libreria_api.model.sistemausuarios.OrigenUsuario;

public class UsuarioResponseDTO {

    private Integer id;
    private String nombre;
    private String correo;
    private String telefono;
    private Boolean activo;
    private String docnum;
    private String origen;

    private Integer rolId;
    private String rolNombre;

    private Integer tipdocId;
    private String tipdocNombre;


    public UsuarioResponseDTO() {
    }

    // Constructor completo
    public UsuarioResponseDTO(Integer id, String nombre, String correo, String telefono,
                              Boolean activo, String docnum, String origen,
                              Integer rolId, String rolNombre,
                              Integer tipdocId, String tipdocNombre) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.activo = activo;
        this.docnum = docnum;
        this.origen = origen;
        this.rolId = rolId;
        this.rolNombre = rolNombre;
        this.tipdocId = tipdocId;
        this.tipdocNombre = tipdocNombre;
    }


    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public String getDocnum() { return docnum; }
    public void setDocnum(String docnum) { this.docnum = docnum; }

    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }

    public Integer getRolId() { return rolId; }
    public void setRolId(Integer rolId) { this.rolId = rolId; }

    public String getRolNombre() { return rolNombre; }
    public void setRolNombre(String rolNombre) { this.rolNombre = rolNombre; }

    public Integer getTipdocId() { return tipdocId; }
    public void setTipdocId(Integer tipdocId) { this.tipdocId = tipdocId; }

    public String getTipdocNombre() { return tipdocNombre; }
    public void setTipdocNombre(String tipdocNombre) { this.tipdocNombre = tipdocNombre; }
}
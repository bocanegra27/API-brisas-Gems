package com.example.libreria_api.dto.experienciausuarios;

import jakarta.validation.constraints.*;

public class ContactoFormularioUpdateDTO {

    private Integer usuarioId;      // Cliente asociado
    private Integer usuarioIdAdmin; // Admin que atiende

    @Pattern(regexp = "formulario|whatsapp")
    private String via;

    @Pattern(regexp = "pendiente|atendido|archivado")
    private String estado;

    @Size(max = 500)
    private String notas;

    // ===== Constructores =====
    public ContactoFormularioUpdateDTO() {}

    public ContactoFormularioUpdateDTO(Integer usuarioId, Integer usuarioIdAdmin, String via, String estado, String notas) {
        this.usuarioId = usuarioId;
        this.usuarioIdAdmin = usuarioIdAdmin;
        this.via = via;
        this.estado = estado;
        this.notas = notas;
    }

    // ===== Getters y Setters =====
    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }

    public Integer getUsuarioIdAdmin() { return usuarioIdAdmin; }
    public void setUsuarioIdAdmin(Integer usuarioIdAdmin) { this.usuarioIdAdmin = usuarioIdAdmin; }

    public String getVia() { return via; }
    public void setVia(String via) { this.via = via; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    @Override
    public String toString() {
        return "ContactoFormularioUpdateDTO{" +
                "usuarioId=" + usuarioId +
                ", usuarioIdAdmin=" + usuarioIdAdmin +
                ", via='" + via + '\'' +
                ", estado='" + estado + '\'' +
                ", notas='" + notas + '\'' +
                '}';
    }
}

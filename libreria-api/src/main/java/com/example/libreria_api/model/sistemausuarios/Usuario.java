package com.example.libreria_api.model.sistemausuarios;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_usuarios_correo", columnNames = "usu_correo"),
                @UniqueConstraint(name = "uk_usuarios_docnum", columnNames = "usu_docnum")
        })
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usu_id")
    private Integer usuId;

    @Column(name = "usu_nombre", nullable = false, length = 150)
    private String usuNombre;

    @Column(name = "usu_correo", nullable = false, length = 100, unique = true)
    private String usuCorreo;

    @Column(name = "usu_telefono", length = 20)
    private String usuTelefono;

    @Column(name = "usu_password", nullable = false, length = 255)
    private String usuPassword;

    @Column(name = "usu_docnum", length = 20)
    private String usuDocnum;

    @Enumerated(EnumType.STRING)
    @Column(name = "usu_origen", nullable = false)
    private OrigenUsuario usuOrigen = OrigenUsuario.formulario;

    @Column(name = "usu_activo", nullable = false)
    private Boolean usuActivo = false;

    // ===== Relaciones =====

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rol_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_usuarios_rol"))
    private Rol rol;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "tipdoc_id",
            foreignKey = @ForeignKey(name = "fk_usuarios_tipodoc"))
    private TipoDeDocumento tipoDocumento;

    // ===== Constructores =====
    public Usuario() {
    }

    public Usuario(String usuNombre, String usuCorreo, String usuTelefono, String usuPassword,
                   String usuDocnum, OrigenUsuario usuOrigen, Boolean usuActivo,
                   Rol rol, TipoDeDocumento tipoDocumento) {
        this.usuNombre = usuNombre;
        this.usuCorreo = usuCorreo;
        this.usuTelefono = usuTelefono;
        this.usuPassword = usuPassword;
        this.usuDocnum = usuDocnum;
        this.usuOrigen = usuOrigen;
        this.usuActivo = usuActivo;
        this.rol = rol;
        this.tipoDocumento = tipoDocumento;
    }

    // ===== Getters y Setters =====
    public Integer getUsuId() { return usuId; }
    public void setUsuId(Integer usuId) { this.usuId = usuId; }

    public String getUsuNombre() { return usuNombre; }
    public void setUsuNombre(String usuNombre) { this.usuNombre = usuNombre; }

    public String getUsuCorreo() { return usuCorreo; }
    public void setUsuCorreo(String usuCorreo) { this.usuCorreo = usuCorreo; }

    public String getUsuTelefono() { return usuTelefono; }
    public void setUsuTelefono(String usuTelefono) { this.usuTelefono = usuTelefono; }

    public String getUsuPassword() { return usuPassword; }
    public void setUsuPassword(String usuPassword) { this.usuPassword = usuPassword; }

    public String getUsuDocnum() { return usuDocnum; }
    public void setUsuDocnum(String usuDocnum) { this.usuDocnum = usuDocnum; }

    public OrigenUsuario getUsuOrigen() { return usuOrigen; }
    public void setUsuOrigen(OrigenUsuario usuOrigen) { this.usuOrigen = usuOrigen; }

    public Boolean getUsuActivo() { return usuActivo; }
    public void setUsuActivo(Boolean usuActivo) { this.usuActivo = usuActivo; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public TipoDeDocumento getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(TipoDeDocumento tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    // ===== MÉTODOS DE USERDETAILS - CORREGIDOS =====

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // CORRECCIÓN: Agregar prefijo "ROLE_" y convertir a mayúsculas
        String roleName = rol.getRolNombre().toUpperCase();
        if (!roleName.startsWith("ROLE_")) {
            roleName = "ROLE_" + roleName;
        }
        return List.of(new SimpleGrantedAuthority(roleName));
    }

    @Override
    public String getPassword() {
        return this.usuPassword;
    }

    @Override
    public String getUsername() {
        return this.usuCorreo;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.usuActivo;
    }
}
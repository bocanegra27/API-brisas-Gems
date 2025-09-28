package com.example.libreria_api.model.experienciausuarios;

import com.example.libreria_api.model.sistemausuarios.Usuario;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contacto_formulario")
public class ContactoFormulario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "con_id")
    private int conId;

    @Column(name = "con_nombre", nullable = false, length = 150)
    private String conNombre;

    // --- LÍNEA CORREGIDA ---
    // Aseguramos que la variable 'conCorreo' esté correctamente mapeada a la columna 'con_correo'
    @Column(name = "con_correo", length = 100)
    private String conCorreo;

    @Column(name = "con_telefono", length = 30)
    private String conTelefono;

    @Column(name = "con_mensaje", nullable = false, columnDefinition = "TEXT")
    private String conMensaje;

    @Column(name = "con_fecha_envio", nullable = false)
    private LocalDateTime conFechaEnvio;

    @Enumerated(EnumType.STRING)
    @Column(name = "con_via", nullable = false, columnDefinition = "enum('formulario','whatsapp')")
    private ViaContacto conVia = ViaContacto.formulario;

    @Column(name = "con_terminos", nullable = false)
    private boolean conTerminos;

    @Enumerated(EnumType.STRING)
    @Column(name = "con_estado", nullable = false, columnDefinition = "enum('pendiente','atendido','archivado')")
    private EstadoContacto conEstado = EstadoContacto.pendiente;

    @Column(name = "con_notas", length = 500)
    private String conNotas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usu_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usu_id_admin")
    private Usuario usuarioAdmin;

    // Constructores, Getters y Setters no necesitan cambios

    public ContactoFormulario() {}

    public int getConId() { return conId; }
    public void setConId(int conId) { this.conId = conId; }

    public String getConNombre() { return conNombre; }
    public void setConNombre(String conNombre) { this.conNombre = conNombre; }

    public String getConCorreo() { return conCorreo; }
    public void setConCorreo(String conCorreo) { this.conCorreo = conCorreo; }

    public String getConTelefono() { return conTelefono; }
    public void setConTelefono(String conTelefono) { this.conTelefono = conTelefono; }

    public String getConMensaje() { return conMensaje; }
    public void setConMensaje(String conMensaje) { this.conMensaje = conMensaje; }

    public LocalDateTime getConFechaEnvio() { return conFechaEnvio; }
    public void setConFechaEnvio(LocalDateTime conFechaEnvio) { this.conFechaEnvio = conFechaEnvio; }

    public ViaContacto getConVia() { return conVia; }
    public void setConVia(ViaContacto conVia) { this.conVia = conVia; }

    public boolean isConTerminos() { return conTerminos; }
    public void setConTerminos(boolean conTerminos) { this.conTerminos = conTerminos; }

    public EstadoContacto getConEstado() { return conEstado; }
    public void setConEstado(EstadoContacto conEstado) { this.conEstado = conEstado; }

    public String getConNotas() { return conNotas; }
    public void setConNotas(String conNotas) { this.conNotas = conNotas; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Usuario getUsuarioAdmin() { return usuarioAdmin; }
    public void setUsuarioAdmin(Usuario usuarioAdmin) { this.usuarioAdmin = usuarioAdmin; }
}
package com.example.libreria_api.model.personalizacionproductos;

import com.example.libreria_api.model.sistemausuarios.Usuario;
import com.example.libreria_api.model.sistemausuarios.SesionAnonima;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "personalizacion")
public class Personalizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "per_id")
    private Integer perId;

    @Column(name = "per_fecha", nullable = false)
    private LocalDate perFecha;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usu_id_cliente", referencedColumnName = "usu_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ses_id", referencedColumnName = "ses_id")
    private SesionAnonima sesion;

    @OneToMany(mappedBy = "personalizacion", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    @OrderBy("detId ASC")
    private List<DetallePersonalizacion> detalles;

    // Constructores
    public Personalizacion() {
    }

    public Personalizacion(Integer perId) {
        this.perId = perId;
    }

    // Getters y Setters
    public Integer getPerId() {
        return perId;
    }

    public void setPerId(Integer perId) {
        this.perId = perId;
    }

    public LocalDate getPerFecha() {
        return perFecha;
    }

    public void setPerFecha(LocalDate perFecha) {
        this.perFecha = perFecha;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public SesionAnonima getSesion() {
        return sesion;
    }

    public void setSesion(SesionAnonima sesion) {
        this.sesion = sesion;
    }

    public List<DetallePersonalizacion> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePersonalizacion> detalles) {
        this.detalles = detalles;
    }

    // Metodo helper
    public boolean esAnonima() {
        return usuario == null && sesion != null;
    }
}
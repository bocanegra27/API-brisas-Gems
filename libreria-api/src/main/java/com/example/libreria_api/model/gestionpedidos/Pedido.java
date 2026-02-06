package com.example.libreria_api.model.gestionpedidos;

import com.example.libreria_api.model.personalizacionproductos.Personalizacion;
import com.example.libreria_api.model.sistemausuarios.SesionAnonima;
import com.example.libreria_api.model.sistemausuarios.Usuario;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ped_id;

    @Column(name = "ped_codigo")
    private String pedCodigo;

    @Column(name = "ped_fecha_creacion")
    private Date pedFechaCreacion;

    @Column(name = "ped_comentarios")
    private String pedComentarios;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "est_id")
    private EstadoPedido estadoPedido;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "per_id")
    private Personalizacion personalizacion;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usu_id_empleado")
    private Usuario empleadoAsignado;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usu_id_cliente")
    private Usuario cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ses_id")
    private SesionAnonima sesion;


    @Column(name = "con_id")
    private Integer conId;

    @Column(name = "ped_identificador_cliente", length = 150)
    private String pedIdentificadorCliente;

    public Pedido() {
    }



    public int getPed_id() { return ped_id; }
    public void setPed_id(int ped_id) { this.ped_id = ped_id; }
    public String getPedCodigo() { return pedCodigo; }
    public void setPedCodigo(String pedCodigo) { this.pedCodigo = pedCodigo; }
    public Date getPedFechaCreacion() { return pedFechaCreacion; }
    public void setPedFechaCreacion(Date pedFechaCreacion) { this.pedFechaCreacion = pedFechaCreacion; }
    public String getPedComentarios() { return pedComentarios; }
    public void setPedComentarios(String pedComentarios) { this.pedComentarios = pedComentarios; }
    public EstadoPedido getEstadoPedido() { return estadoPedido; }
    public void setEstadoPedido(EstadoPedido estadoPedido) { this.estadoPedido = estadoPedido; }

    // Usar Entidad Personalizacion
    public Personalizacion getPersonalizacion() { return personalizacion; }
    public void setPersonalizacion(Personalizacion personalizacion) { this.personalizacion = personalizacion; }

    // Usar Entidad Empleado
    public Usuario getEmpleadoAsignado() { return empleadoAsignado; }
    public void setEmpleadoAsignado(Usuario empleadoAsignado) { this.empleadoAsignado = empleadoAsignado; }

    // NUEVO: Usar Entidad Cliente
    public Usuario getCliente() { return cliente; }
    public void setCliente(Usuario cliente) { this.cliente = cliente; }

    public SesionAnonima getSesion() { return sesion; }
    public void setSesion(SesionAnonima sesion) { this.sesion = sesion; }
    public Integer getConId() { return conId; }
    public void setConId(Integer conId) { this.conId = conId; }
    public String getPedIdentificadorCliente() { return pedIdentificadorCliente; }
    public void setPedIdentificadorCliente(String pedIdentificadorCliente) { this.pedIdentificadorCliente = pedIdentificadorCliente; }
}
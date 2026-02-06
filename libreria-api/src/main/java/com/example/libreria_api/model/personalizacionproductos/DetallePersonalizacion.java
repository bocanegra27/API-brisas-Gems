package com.example.libreria_api.model.personalizacionproductos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "detalle_personalizacion")
public class DetallePersonalizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "det_id")
    private int detId;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "per_id", referencedColumnName = "per_id", nullable = false)
    @JsonBackReference
    private Personalizacion personalizacion;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "val_id", referencedColumnName = "val_id", nullable = false)
    private ValorPersonalizacion valorPersonalizacion;

    public DetallePersonalizacion() { }

    public DetallePersonalizacion(Personalizacion personalizacion, ValorPersonalizacion valorPersonalizacion) {
        this.personalizacion = personalizacion;
        this.valorPersonalizacion = valorPersonalizacion;
    }

    public int getDetId() { return detId; }
    public void setDetId(int detId) { this.detId = detId; }

    public Personalizacion getPersonalizacion() { return personalizacion; }
    public void setPersonalizacion(Personalizacion personalizacion) { this.personalizacion = personalizacion; }

    public ValorPersonalizacion getValorPersonalizacion() { return valorPersonalizacion; }
    public void setValorPersonalizacion(ValorPersonalizacion valorPersonalizacion) { this.valorPersonalizacion = valorPersonalizacion; }
}

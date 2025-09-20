package com.example.libreria_api.model.sistemausuarios;

import jakarta.persistence.*;

@Entity
@Table(name = "rol")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rolId;

    @Column(name = "rol_nombre")
    private String rolNombre;

    public Rol() {
    }

    public Rol(String rolNombre) {
        this.rolNombre = rolNombre;
    }


    public int getRolId() {
        return rolId;
    }

    public void setRolId(int rol_id) {
        this.rolId = rol_id;
    }

    public String getRolNombre() {
        return rolNombre;
    }

    public void setRolNombre(String rolNombre) {
        this.rolNombre = rolNombre;
    }
}
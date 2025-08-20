package com.example.libreria_api.controller;

import com.example.libreria_api.model.Rol;
import com.example.libreria_api.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class RolController {

    @Autowired
    private RolService rolService;

    @GetMapping("/roles")
    public List<Rol> obtenerTodosLosRoles() {
        return rolService.obtenerTodosLosRoles();
    }

    @PostMapping("/roles")
    public Rol crearRol(@RequestBody Rol rol) {
        return rolService.guardarRol(rol);
    }
}
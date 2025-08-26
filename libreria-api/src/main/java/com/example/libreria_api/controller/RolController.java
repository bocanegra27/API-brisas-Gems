package com.example.libreria_api.controller;

import com.example.libreria_api.model.Pedido;
import com.example.libreria_api.model.Rol;
import com.example.libreria_api.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("roles/{id}")
    public Rol actualizar(@PathVariable Integer id, @RequestBody Rol rol) {
        return rolService.actualizar(id, rol);
    }
}
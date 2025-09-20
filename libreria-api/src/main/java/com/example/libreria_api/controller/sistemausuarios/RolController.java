package com.example.libreria_api.controller.sistemausuarios;

import com.example.libreria_api.model.sistemausuarios.Rol;
import com.example.libreria_api.service.sistemausuarios.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    @Autowired
    private RolService rolService;

    @GetMapping
    public List<Rol> obtenerTodosLosRoles() {
        return rolService.obtenerTodosLosRoles();
    }

    @PostMapping
    public Rol crearRol(@RequestBody Rol rol) {
        return rolService.guardarRol(rol);
    }

    @PutMapping("/{id}")
    public Rol actualizar(@PathVariable Integer id, @RequestBody Rol rol) {
        return rolService.actualizar(id, rol);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id){
        rolService.eliminarRol(id);
    }
}
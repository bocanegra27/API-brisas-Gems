package com.example.libreria_api.controller.experienciausuarios;


import com.example.libreria_api.model.experienciausuarios.PortafolioInspiracion;
import com.example.libreria_api.service.experienciausuarios.PortafolioInspiracionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PortafolioInspiracionController {

    @Autowired
    private PortafolioInspiracionService portafolioInspiracionService;

    @GetMapping("/portafolio")
    public List<PortafolioInspiracion> obtenerPortafolio() {
        return portafolioInspiracionService.obtenerPortafolio();
    }

    @PostMapping("/portafolio")
    public PortafolioInspiracion guardarPortafolio(@RequestBody PortafolioInspiracion portafolioInspiracion) {
        return portafolioInspiracionService.guardarPortafolio(portafolioInspiracion);
    }

    @PutMapping("/portafolio/{id}")
    public PortafolioInspiracion actualizarPortafolio(@PathVariable Integer id, @RequestBody PortafolioInspiracion portafolioInspiracion) {
        return portafolioInspiracionService.actualizar(id, portafolioInspiracion);
    }

    @DeleteMapping("/portafolio/{id}")
    public void eliminarPortafolio(@PathVariable Integer id){
        portafolioInspiracionService.eliminarPortafolio(id);
    }
}


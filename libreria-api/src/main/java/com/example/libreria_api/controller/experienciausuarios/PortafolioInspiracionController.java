package com.example.libreria_api.controller.experienciausuarios;


import com.example.libreria_api.model.experienciausuarios.PortafolioInspiracion;
import com.example.libreria_api.service.experienciausuarios.PortafolioInspiracionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name="Portafolio de Inpiración", description = "Gestión de los elementos de la galería pública o " +
        "portafolio que sirven como ejemplos e inspiración para los usuarios.")
@RequestMapping("/api/portafolio")
public class PortafolioInspiracionController {

    @Autowired
    private PortafolioInspiracionService portafolioInspiracionService;

    @GetMapping
    @Operation(summary = "Obtener el portafolio completo",
    description = "Recupera la lista de todos los elementos (proyectos, imágenes, referencias) que componen el portafolio de inspiración.")
    public List<PortafolioInspiracion> obtenerPortafolio() {
        return portafolioInspiracionService.obtenerPortafolio();
    }

    @PostMapping
    @Operation(summary = "Agregar elemento al portafolio",
    description = "Crea y añade un nuevo elemento al catálogo del portafolio.")
    public PortafolioInspiracion guardarPortafolio(@RequestBody PortafolioInspiracion portafolioInspiracion) {
        return portafolioInspiracionService.guardarPortafolio(portafolioInspiracion);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un elemento del portafolio",
    description = "Modifica los detalles de un elemento del portafolio existente, identificado por su ID.")
    public PortafolioInspiracion actualizarPortafolio(@PathVariable Integer id, @RequestBody PortafolioInspiracion portafolioInspiracion) {
        return portafolioInspiracionService.actualizar(id, portafolioInspiracion);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar elemento del portafolio",
    description = "Elimina permanentemente un elemento específico de la galería de inspiración.")
    public void eliminarPortafolio(@PathVariable Integer id){
        portafolioInspiracionService.eliminarPortafolio(id);
    }
}


package com.example.libreria_api.controller;

import com.example.libreria_api.model.Personalizacion;
import com.example.libreria_api.service.PersonalizacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PersonalizacionController {

    @Autowired
    private PersonalizacionService personalizacionService;

    @GetMapping("/personalizacion")
    public List<Personalizacion> obtenerPersonalizaciones() {
        return personalizacionService.obtenerPersonalizaciones();
    }

    @PostMapping("/personalizacion")
    public Personalizacion crearPersonalizacion(@RequestBody Personalizacion personalizacion) {
        return personalizacionService.crearPersonalizacion(personalizacion);
    }

    @PutMapping("/personalizacion/{id}")
    public Personalizacion actualizarPersonalizacion(@PathVariable Integer id, @RequestBody Personalizacion personalizacion) {
        return personalizacionService.actualizarPersonalizacion(id,personalizacion);
    }

    @DeleteMapping("/personalizacion/{id}")
    public void eliminarPersonalizacion(@PathVariable Integer id){
        personalizacionService.eliminarPersonalizacion(id);
    }
}


package com.example.libreria_api.controller;

import com.example.libreria_api.model.OpcionPersonalizacion;
import com.example.libreria_api.service.OpcionPersonalizacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class OpcionPersonalizacionController {

    @Autowired
    private OpcionPersonalizacionService opcionPersonalizacionService;

    @GetMapping("/opciones-personalizacion")
    public List<OpcionPersonalizacion> obtenerTodos() {
        return opcionPersonalizacionService.obtenerTodos();
    }

    @PostMapping("/opciones-personalizacion")
    public OpcionPersonalizacion crear(@RequestBody OpcionPersonalizacion opcionPersonalizacion) {
        return opcionPersonalizacionService.guardar(opcionPersonalizacion);
    }
}
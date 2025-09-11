package com.example.libreria_api.controller.personalizacionproductos;

import com.example.libreria_api.model.personalizacionproductos.OpcionPersonalizacion;
import com.example.libreria_api.service.personalizacionproductos.OpcionPersonalizacionService;
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

    @PutMapping("opciones-personalizacion/{id}")
    public OpcionPersonalizacion actualizar(@PathVariable Integer id, @RequestBody OpcionPersonalizacion detalles) {
        return opcionPersonalizacionService.actualizar(id, detalles);
    }

    @DeleteMapping("/opciones-personalizacion/{id}")
    public void eliminar(@PathVariable Integer id) {
        opcionPersonalizacionService.eliminar(id);
    }
}
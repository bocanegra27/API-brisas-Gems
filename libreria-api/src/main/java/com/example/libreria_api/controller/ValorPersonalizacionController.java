package com.example.libreria_api.controller;

import com.example.libreria_api.model.ValorPersonalizacion;
import com.example.libreria_api.service.ValorPersonalizacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/valores-personalizacion")
public class ValorPersonalizacionController {

    @Autowired
    private ValorPersonalizacionService valorPersonalizacionService;

    // --- GET: Obtener todos ---
    @GetMapping
    public ResponseEntity<List<ValorPersonalizacion>> obtenerTodos() {
        List<ValorPersonalizacion> lista = valorPersonalizacionService.obtenerTodos();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    // --- GET: Obtener por ID ---
    @GetMapping("/{id}")
    public ResponseEntity<ValorPersonalizacion> obtenerPorId(@PathVariable Integer id) {
        return valorPersonalizacionService.obtenerPorId(id)
                .map(valor -> new ResponseEntity<>(valor, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // --- POST: Crear ---
    @PostMapping
    public ResponseEntity<ValorPersonalizacion> crear(@RequestBody ValorPersonalizacion valorPersonalizacion) {
        ValorPersonalizacion nuevo = valorPersonalizacionService.guardar(valorPersonalizacion);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    // --- PUT: Actualizar ---
    @PutMapping("/{id}")
    public ResponseEntity<ValorPersonalizacion> actualizar(@PathVariable Integer id, @RequestBody ValorPersonalizacion detalles) {
        ValorPersonalizacion actualizado = valorPersonalizacionService.actualizar(id, detalles);
        if (actualizado != null) {
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // --- DELETE: Eliminar ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        boolean eliminado = valorPersonalizacionService.eliminar(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

package com.example.libreria_api.controller;

import com.example.libreria_api.model.Render3d;
import com.example.libreria_api.service.Render3dService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/renders") // Ruta base para los renders
public class Render3dController {

    @Autowired
    private Render3dService render3dService;

    @GetMapping
    public ResponseEntity<List<Render3d>> obtenerTodosLosRenders() {
        return new ResponseEntity<>(render3dService.obtenerTodosLosRenders(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Render3d> obtenerRenderPorId(@PathVariable Integer id) {
        return render3dService.obtenerRenderPorId(id)
                .map(render -> new ResponseEntity<>(render, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Render3d> crearRender(@RequestBody Render3d render) {
        return new ResponseEntity<>(render3dService.guardarRender(render), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Render3d> actualizarRender(@PathVariable Integer id, @RequestBody Render3d renderDetalles) {
        Render3d renderActualizado = render3dService.actualizarRender(id, renderDetalles);
        if (renderActualizado != null) {
            return new ResponseEntity<>(renderActualizado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRender(@PathVariable Integer id) {
        if (render3dService.eliminarRender(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
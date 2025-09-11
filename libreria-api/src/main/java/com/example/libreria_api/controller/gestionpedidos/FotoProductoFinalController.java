package com.example.libreria_api.controller.gestionpedidos;

import com.example.libreria_api.model.gestionpedidos.FotoProductoFinal;
import com.example.libreria_api.service.gestionpedidos.FotoProductoFinalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/fotos")
public class FotoProductoFinalController {

    @Autowired
    private FotoProductoFinalService fotoProductoFinalService;

    @GetMapping
    public ResponseEntity<List<FotoProductoFinal>> obtenerTodasLasFotos() {
        List<FotoProductoFinal> fotos = fotoProductoFinalService.obtenerTodasLasFotos();
        return new ResponseEntity<>(fotos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FotoProductoFinal> obtenerFotoPorId(@PathVariable Integer id) {
        return fotoProductoFinalService.obtenerFotoPorId(id)
                .map(foto -> new ResponseEntity<>(foto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Endpoint para crear una nueva foto
    @PostMapping
    public ResponseEntity<FotoProductoFinal> crearFoto(@RequestBody FotoProductoFinal foto) {
        FotoProductoFinal nuevaFoto = fotoProductoFinalService.guardarFoto(foto);
        return new ResponseEntity<>(nuevaFoto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FotoProductoFinal> actualizarFoto(@PathVariable Integer id, @RequestBody FotoProductoFinal fotoDetalles) {
        FotoProductoFinal fotoActualizada = fotoProductoFinalService.actualizarFoto(id, fotoDetalles);
        if (fotoActualizada != null) {
            return new ResponseEntity<>(fotoActualizada, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarFoto(@PathVariable Integer id) {
        boolean eliminado = fotoProductoFinalService.eliminarFoto(id);
        if (eliminado) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
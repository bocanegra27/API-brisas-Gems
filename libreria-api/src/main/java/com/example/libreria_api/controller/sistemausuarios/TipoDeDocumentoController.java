package com.example.libreria_api.controller.sistemausuarios;

import com.example.libreria_api.model.sistemausuarios.TipoDeDocumento;
import com.example.libreria_api.service.sistemausuarios.TipoDeDocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tipos-documento")
public class TipoDeDocumentoController {

    @Autowired
    private TipoDeDocumentoService tipoDeDocumentoService;

    @GetMapping
    public List<TipoDeDocumento> obtenerTodos() {
        return tipoDeDocumentoService.obtenerTodos();
    }

    @PostMapping
    public TipoDeDocumento crear(@RequestBody TipoDeDocumento tipoDeDocumento) {
        return tipoDeDocumentoService.guardar(tipoDeDocumento);
    }

    @PutMapping("/{id}")
    public TipoDeDocumento actualizar(@PathVariable Integer id, @RequestBody TipoDeDocumento detalles) {
        return tipoDeDocumentoService.actualizar(id, detalles);
    }

    @DeleteMapping ("/{id}")
    public void eliminardocumento(@PathVariable Integer id) {
        tipoDeDocumentoService.eliminardocumento(id);
    }

}
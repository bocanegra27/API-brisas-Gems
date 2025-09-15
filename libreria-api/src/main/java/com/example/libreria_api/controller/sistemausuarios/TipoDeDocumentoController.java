package com.example.libreria_api.controller.sistemausuarios;

import com.example.libreria_api.model.sistemausuarios.TipoDeDocumento;
import com.example.libreria_api.service.sistemausuarios.TipoDeDocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class TipoDeDocumentoController {

    @Autowired
    private TipoDeDocumentoService tipoDeDocumentoService;

    @GetMapping("/tipos-documento")
    public List<TipoDeDocumento> obtenerTodos() {
        return tipoDeDocumentoService.obtenerTodos();
    }

    @PostMapping("/tipos-documento")
    public TipoDeDocumento crear(@RequestBody TipoDeDocumento tipoDeDocumento) {
        return tipoDeDocumentoService.guardar(tipoDeDocumento);
    }

    @PutMapping("tipos-documento/{id}")
    public TipoDeDocumento actualizar(@PathVariable Integer id, @RequestBody TipoDeDocumento detalles) {
        return tipoDeDocumentoService.actualizar(id, detalles);
    }

    @DeleteMapping ("tipos-documento/{id}")
    public void eliminardocumento(@PathVariable Integer id) {
        tipoDeDocumentoService.eliminardocumento(id);
    }

}
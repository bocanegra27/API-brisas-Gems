package com.example.libreria_api.controller.sistemausuarios;

import com.example.libreria_api.model.sistemausuarios.TipoDeDocumento;
import com.example.libreria_api.service.sistemausuarios.TipoDeDocumentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Tag(name="Tipo de documento",description = " Gestión del catálogo de los diferentes " +
        "tipos de documentos de identidad permitidos en el sistema")
@RequestMapping("/api/tipos-documento")
public class TipoDeDocumentoController {

    @Autowired
    private TipoDeDocumentoService tipoDeDocumentoService;

    @GetMapping
    @Operation(summary = "Obtener todos los tipos de documento",
    description = "Recupera la lista completa de todos los tipos de documentos de" +
            " identidad configurados en el sistema.")
    public List<TipoDeDocumento> obtenerTodos() {
        return tipoDeDocumentoService.obtenerTodos();
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo tipo de documento",
    description = "Añade un nuevo tipo de documento al catálogo")
    public TipoDeDocumento crear(@RequestBody TipoDeDocumento tipoDeDocumento) {
        return tipoDeDocumentoService.guardar(tipoDeDocumento);
    }

    @PutMapping("/{id}")
    @Operation (summary = "Actualizar un tipo de documento",
    description = "Modifica los detalles de un tipo de documento existente, " +
            "identificado por su ID.")
    public TipoDeDocumento actualizar(@PathVariable Integer id, @RequestBody TipoDeDocumento detalles) {
        return tipoDeDocumentoService.actualizar(id, detalles);
    }

    @DeleteMapping ("/{id}")
    @Operation(summary = "Eliminar un tipo de documento",
    description = "Elimina un tipo de documento específico del catálogo.")
    public void eliminardocumento(@PathVariable Integer id) {
        tipoDeDocumentoService.eliminardocumento(id);
    }

}
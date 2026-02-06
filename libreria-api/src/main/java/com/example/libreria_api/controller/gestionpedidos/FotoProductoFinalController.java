package com.example.libreria_api.controller.gestionpedidos;

import com.example.libreria_api.dto.gestionpedidos.FotoProductoFinalRequestDTO;
import com.example.libreria_api.dto.gestionpedidos.FotoProductoFinalResponseDTO;
import com.example.libreria_api.service.gestionpedidos.FotoProductoFinalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@Tag(name="Fotos de Producto Final",
description = "Gestión de las imágenes (fotos) que documentan el resultado o el " +
        "producto final asociado a un pedido.")
@RequestMapping("/api/fotos")
public class FotoProductoFinalController {

    private final FotoProductoFinalService fotoProductoFinalService;

     public FotoProductoFinalController(FotoProductoFinalService fotoProductoFinalService) {
        this.fotoProductoFinalService = fotoProductoFinalService;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las fotos de producto final",
    description = "Recupera una lista completa de todas las fotos de producto final " +
            "registradas en el sistema.")
    public ResponseEntity<List<FotoProductoFinalResponseDTO>> obtenerTodasLasFotos() {
        List<FotoProductoFinalResponseDTO> fotos = fotoProductoFinalService.obtenerTodasLasFotos();
        return ResponseEntity.ok(fotos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener foto por ID",
    description = "Busca y devuelve los detalles de una foto de producto final específica utilizando " +
            "su identificador único.")
    public ResponseEntity<FotoProductoFinalResponseDTO> obtenerFotoPorId(@PathVariable Integer id) {
        return fotoProductoFinalService.obtenerFotoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/subir/{pedidoId}")
    public ResponseEntity<FotoProductoFinalResponseDTO> crearFoto(
            @PathVariable Integer pedidoId,
            @RequestParam("archivo") MultipartFile archivo) {
        try {
            FotoProductoFinalResponseDTO nuevaFoto = fotoProductoFinalService.guardarFoto(pedidoId, archivo);
            return new ResponseEntity<>(nuevaFoto, HttpStatus.CREATED);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar registro de foto",
    description = "Modifica los detalles (metadatos, descripción, etc.) de una foto de producto final existente.")
    public ResponseEntity<FotoProductoFinalResponseDTO> actualizarFoto(@PathVariable Integer id, @RequestBody FotoProductoFinalRequestDTO requestDTO) {
        try {
            FotoProductoFinalResponseDTO fotoActualizada = fotoProductoFinalService.actualizarFoto(id, requestDTO);
            return ResponseEntity.ok(fotoActualizada);
        } catch (RuntimeException e) {
             return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar registro de foto",
    description = "Elimina la referencia a una foto de producto final del sistema.")
    public ResponseEntity<Void> eliminarFoto(@PathVariable Integer id) {
        boolean eliminado = fotoProductoFinalService.eliminarFoto(id);
        if (eliminado) {
            return ResponseEntity.noContent().build(); // HTTP 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // HTTP 404 Not Found
        }
    }
}
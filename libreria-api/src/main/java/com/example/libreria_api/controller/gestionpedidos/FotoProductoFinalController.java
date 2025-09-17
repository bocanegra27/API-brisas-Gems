package com.example.libreria_api.controller.gestionpedidos;

import com.example.libreria_api.dto.gestionpedidos.FotoProductoFinalRequestDTO;
import com.example.libreria_api.dto.gestionpedidos.FotoProductoFinalResponseDTO;
import com.example.libreria_api.service.gestionpedidos.FotoProductoFinalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fotos")
public class FotoProductoFinalController {

    private final FotoProductoFinalService fotoProductoFinalService;

     public FotoProductoFinalController(FotoProductoFinalService fotoProductoFinalService) {
        this.fotoProductoFinalService = fotoProductoFinalService;
    }

    @GetMapping
    public ResponseEntity<List<FotoProductoFinalResponseDTO>> obtenerTodasLasFotos() {
        List<FotoProductoFinalResponseDTO> fotos = fotoProductoFinalService.obtenerTodasLasFotos();
        return ResponseEntity.ok(fotos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FotoProductoFinalResponseDTO> obtenerFotoPorId(@PathVariable Integer id) {
        return fotoProductoFinalService.obtenerFotoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FotoProductoFinalResponseDTO> crearFoto(@RequestBody FotoProductoFinalRequestDTO requestDTO) {
        FotoProductoFinalResponseDTO nuevaFoto = fotoProductoFinalService.guardarFoto(requestDTO);
        return new ResponseEntity<>(nuevaFoto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FotoProductoFinalResponseDTO> actualizarFoto(@PathVariable Integer id, @RequestBody FotoProductoFinalRequestDTO requestDTO) {
        try {
            FotoProductoFinalResponseDTO fotoActualizada = fotoProductoFinalService.actualizarFoto(id, requestDTO);
            return ResponseEntity.ok(fotoActualizada);
        } catch (RuntimeException e) {
             return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarFoto(@PathVariable Integer id) {
        boolean eliminado = fotoProductoFinalService.eliminarFoto(id);
        if (eliminado) {
            return ResponseEntity.noContent().build(); // HTTP 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // HTTP 404 Not Found
        }
    }
}
package com.example.libreria_api.controller.gestionpedidos;

import com.example.libreria_api.dto.gestionpedidos.Render3dRequestDTO;
import com.example.libreria_api.dto.gestionpedidos.Render3dResponseDTO;
import com.example.libreria_api.service.gestionpedidos.Render3dService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Renders 3D", description = "Gestión de los activos, configuraciones" +
        " o solicitudes de modelos de renderizado 3D dentro del sistema.")
@RequestMapping("/api/renders")
public class Render3dController {

    private final Render3dService render3dService;

     public Render3dController(Render3dService render3dService) {
        this.render3dService = render3dService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los renders 3D",
    description = "Recupera una lista completa de todos los renders " +
            "3D o solicitudes de render guardados en la base de datos.")
    public ResponseEntity<List<Render3dResponseDTO>> obtenerTodosLosRenders() {
        return ResponseEntity.ok(render3dService.obtenerTodosLosRenders());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener render 3D por id",
    description = "Busca y devuelve los detalles de un render 3D específico" +
            " utilizando su identificador único.")
    public ResponseEntity<Render3dResponseDTO> obtenerRenderPorId(@PathVariable Integer id) {
        return render3dService.obtenerRenderPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear una nueva solicitud de render 3D",
    description = "Registra un nuevo activo o solicitud de render 3D con sus parámetros iniciales.")
    public ResponseEntity<Render3dResponseDTO> crearRender(@RequestBody Render3dRequestDTO requestDTO) {
        try {
            Render3dResponseDTO nuevoRender = render3dService.guardarRender(requestDTO);
            return new ResponseEntity<>(nuevoRender, HttpStatus.CREATED);
        } catch (RuntimeException e) {
             return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un render 3D existente",
    description = "Modifica completamente los detalles de un render 3D identificado por su ID.")
    public ResponseEntity<Render3dResponseDTO> actualizarRender(@PathVariable Integer id, @RequestBody Render3dRequestDTO requestDTO) {
        try {
            Render3dResponseDTO renderActualizado = render3dService.actualizarRender(id, requestDTO);
            return ResponseEntity.ok(renderActualizado);
        } catch (RuntimeException e) {
             return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un render 3D",
    description = "Elimina permanentemente un activo o solicitud de render 3D del sistema.")
    public ResponseEntity<Void> eliminarRender(@PathVariable Integer id) {
        if (render3dService.eliminarRender(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
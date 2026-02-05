package com.example.libreria_api.controller.gestionpedidos;

import com.example.libreria_api.dto.gestionpedidos.PedidoResponseDTO;
import com.example.libreria_api.dto.gestionpedidos.Render3dResponseDTO;
import com.example.libreria_api.service.gestionpedidos.Render3dService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Tag(name = "Renders 3D", description = "Gestión de los activos de modelos de renderizado 3D.")
@RequestMapping("/api/renders")
public class Render3dController {

    private final Render3dService render3dService;

    public Render3dController(Render3dService render3dService) {
        this.render3dService = render3dService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los renders 3D")
    public ResponseEntity<List<Render3dResponseDTO>> obtenerTodosLosRenders() {
        return ResponseEntity.ok(render3dService.obtenerTodosLosRenders());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener render 3D por id")
    public ResponseEntity<Render3dResponseDTO> obtenerRenderPorId(@PathVariable Integer id) {
        return render3dService.obtenerRenderPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un render 3D")
    public ResponseEntity<Void> eliminarRender(@PathVariable Integer id) {
        if (render3dService.eliminarRender(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
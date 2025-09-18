package com.example.libreria_api.controller.gestionpedidos;

import com.example.libreria_api.dto.gestionpedidos.Render3dRequestDTO;
import com.example.libreria_api.dto.gestionpedidos.Render3dResponseDTO;
import com.example.libreria_api.service.gestionpedidos.Render3dService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/renders")
public class Render3dController {

    private final Render3dService render3dService;

     public Render3dController(Render3dService render3dService) {
        this.render3dService = render3dService;
    }

    @GetMapping
    public ResponseEntity<List<Render3dResponseDTO>> obtenerTodosLosRenders() {
        return ResponseEntity.ok(render3dService.obtenerTodosLosRenders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Render3dResponseDTO> obtenerRenderPorId(@PathVariable Integer id) {
        return render3dService.obtenerRenderPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Render3dResponseDTO> crearRender(@RequestBody Render3dRequestDTO requestDTO) {
        try {
            Render3dResponseDTO nuevoRender = render3dService.guardarRender(requestDTO);
            return new ResponseEntity<>(nuevoRender, HttpStatus.CREATED);
        } catch (RuntimeException e) {
             return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Render3dResponseDTO> actualizarRender(@PathVariable Integer id, @RequestBody Render3dRequestDTO requestDTO) {
        try {
            Render3dResponseDTO renderActualizado = render3dService.actualizarRender(id, requestDTO);
            return ResponseEntity.ok(renderActualizado);
        } catch (RuntimeException e) {
             return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRender(@PathVariable Integer id) {
        if (render3dService.eliminarRender(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
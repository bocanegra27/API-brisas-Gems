package com.example.libreria_api.controller.personalizacionproductos;

import com.example.libreria_api.dto.personalizacionproductos.CategoriaProductoCreateDTO;
import com.example.libreria_api.dto.personalizacionproductos.CategoriaProductoResponseDTO;
import com.example.libreria_api.service.personalizacionproductos.CategoriaProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name="Categorías de Producto", description = "Gestión de tipos de productos (Anillos, Pulseras, etc.)")
@RequestMapping("/api/categorias")
public class CategoriaProductoController {

    private final CategoriaProductoService categoriaService;

    // Inyectamos el Servicio, no el Repositorio
    public CategoriaProductoController(CategoriaProductoService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    @Operation(summary = "Listar todas las categorías")
    public ResponseEntity<List<CategoriaProductoResponseDTO>> listar() {
        return ResponseEntity.ok(categoriaService.listar());
    }

    // ¡ESTE ES EL MÉODO QUE FALTABA!
    @PostMapping
    @Operation(summary = "Crear nueva categoría")
    public ResponseEntity<?> crear(@Valid @RequestBody CategoriaProductoCreateDTO dto) {
        try {
            CategoriaProductoResponseDTO creada = categoriaService.crear(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creada);
        } catch (Exception e) {
            // Captura errores como nombres duplicados
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear categoría: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una categoría y todo su contenido")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            categoriaService.eliminar(id);
            // CORRECCIÓN: Devolvemos 200 OK con un map para que PHP reciba un JSON real
            return ResponseEntity.ok(java.util.Collections.singletonMap("mensaje", "Categoría eliminada con éxito"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("No se puede eliminar: " + e.getMessage());
        }
    }
}
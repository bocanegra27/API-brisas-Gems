package com.example.libreria_api.controller.personalizacionproductos;

import com.example.libreria_api.dto.personalizacionproductos.CategoriaProductoResponseDTO;
import com.example.libreria_api.model.personalizacionproductos.CategoriaProducto;
import com.example.libreria_api.repository.personalizacionproductos.CategoriaProductoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name="Categorías de Producto", description = "Gestión de tipos de productos (Anillos, Pulseras, etc.)")
@RequestMapping("/api/categorias")
public class CategoriaProductoController {

    private final CategoriaProductoRepository categoriaRepository;

    public CategoriaProductoController(CategoriaProductoRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping
    @Operation(summary = "Listar todas las categorías")
    public ResponseEntity<List<CategoriaProductoResponseDTO>> listar() {
        List<CategoriaProducto> categorias = categoriaRepository.findAll();

        List<CategoriaProductoResponseDTO> dtos = categorias.stream()
                .map(c -> new CategoriaProductoResponseDTO(c.getCatId(), c.getCatNombre(), c.getCatSlug()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
}
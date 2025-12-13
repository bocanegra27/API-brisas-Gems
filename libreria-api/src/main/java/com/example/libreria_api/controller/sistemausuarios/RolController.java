package com.example.libreria_api.controller.sistemausuarios;

import com.example.libreria_api.dto.sistemausuarios.RolResponseDTO;
import com.example.libreria_api.service.sistemausuarios.RolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name="Roles", description = "Consulta del catálogo de roles de usuario " +
        "disponibles en el sistema")
@RequestMapping("/api/roles")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los roles disponibles",
    description = "Recupera una lista completa de todos los roles de" +
            " usuario configurados en el sistema.")
    public ResponseEntity<List<RolResponseDTO>> listarTodos() {
        return ResponseEntity.ok(rolService.listarTodos());
    }
}
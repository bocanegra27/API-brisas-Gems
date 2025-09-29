package com.example.libreria_api.controller.sistemausuarios;

import com.example.libreria_api.dto.sistemausuarios.RolResponseDTO;
import com.example.libreria_api.service.sistemausuarios.RolService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping
    public ResponseEntity<List<RolResponseDTO>> listarTodos() {
        return ResponseEntity.ok(rolService.listarTodos());
    }
}
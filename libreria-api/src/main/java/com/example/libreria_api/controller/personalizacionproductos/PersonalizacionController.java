package com.example.libreria_api.controller.personalizacionproductos;

import com.example.libreria_api.dto.personalizacionproductos.PersonalizacionCreateDTO;
import com.example.libreria_api.dto.personalizacionproductos.PersonalizacionUpdateDTO;
import com.example.libreria_api.dto.personalizacionproductos.PersonalizacionResponseDTO;

import com.example.libreria_api.service.personalizacionproductos.PersonalizacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/personalizaciones")
public class PersonalizacionController {

    @Autowired
    private PersonalizacionService personalizacionService;

    // GET con filtros opcionales
    @GetMapping
    public ResponseEntity<List<PersonalizacionResponseDTO>> obtenerPersonalizaciones(
            @RequestParam(required = false) Integer clienteId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta) {

        // Aquí puedes delegar al service con los filtros
        List<PersonalizacionResponseDTO> resultado = personalizacionService
                .filtrarPersonalizaciones(clienteId, fechaDesde, fechaHasta);

        return ResponseEntity.ok(resultado);
    }

    // GET por id
    @GetMapping("/{id}")
    public ResponseEntity<PersonalizacionResponseDTO> obtenerPorId(@PathVariable Integer id) {
        PersonalizacionResponseDTO dto = personalizacionService.obtenerPorId(id);
        if (dto == null) {
            return ResponseEntity.notFound().build(); // 404
        }
        return ResponseEntity.ok(dto);
    }

    // POST crear
    @PostMapping
    public ResponseEntity<PersonalizacionResponseDTO> crear(@RequestBody PersonalizacionCreateDTO dto) {
        try {
            PersonalizacionResponseDTO creada = personalizacionService.crear(dto);
            return ResponseEntity.ok(creada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // 400
        }
    }

    // PUT actualizar
    @PutMapping("/{id}")
    public ResponseEntity<PersonalizacionResponseDTO> actualizar(
            @PathVariable Integer id,
            @RequestBody PersonalizacionUpdateDTO dto) {

        PersonalizacionResponseDTO actualizada = personalizacionService.actualizar(id, dto);
        if (actualizada == null) {
            return ResponseEntity.notFound().build(); // 404
        }
        return ResponseEntity.ok(actualizada);
    }

    // DELETE eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        boolean eliminado = personalizacionService.eliminar(id);
        if (!eliminado) {
            return ResponseEntity.notFound().build(); // 404
        }
        return ResponseEntity.noContent().build();
    }
}

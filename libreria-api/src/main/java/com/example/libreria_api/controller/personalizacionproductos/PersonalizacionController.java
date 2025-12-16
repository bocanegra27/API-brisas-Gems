package com.example.libreria_api.controller.personalizacionproductos;

import com.example.libreria_api.dto.personalizacionproductos.PersonalizacionCreateDTO;
import com.example.libreria_api.dto.personalizacionproductos.PersonalizacionUpdateDTO;
import com.example.libreria_api.dto.personalizacionproductos.PersonalizacionResponseDTO;

import com.example.libreria_api.service.personalizacionproductos.PersonalizacionService;
import com.example.libreria_api.service.seguridad.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Tag(name="Personalizaciones de Cliente",
description = "Gestión de los registros detallados de las opciones y valores elegidos por un cliente para " +
        "personalizar un producto o servicio.")
@RequestMapping("/api/personalizaciones")
public class PersonalizacionController {

    @Autowired
    private PersonalizacionService personalizacionService;


    @GetMapping
    @Operation(summary = "Listar y filtrar personalizaciones",
    description = "Obtiene una lista de registros de personalización. Permite filtrar por clienteId" +
            " y rangos de fecha de creación")
    public ResponseEntity<List<PersonalizacionResponseDTO>> obtenerPersonalizaciones(
            @RequestParam(required = false) Integer clienteId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime fechaHasta) {

        // Aquí puedes delegar al service con los filtros
        List<PersonalizacionResponseDTO> resultado = personalizacionService
                .filtrarPersonalizaciones(clienteId, fechaDesde, fechaHasta);

        return ResponseEntity.ok(resultado);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Obtener personalización por ID",
    description = "Busca y devuelve los detalles de un registro de personalización específico" +
            " utilizando su identificador único.")
    public ResponseEntity<PersonalizacionResponseDTO> obtenerPorId(@PathVariable Integer id) {
        PersonalizacionResponseDTO dto = personalizacionService.obtenerPorId(id);
        if (dto == null) {
            return ResponseEntity.notFound().build(); // 404
        }
        return ResponseEntity.ok(dto);
    }


    @PostMapping
    @Operation(summary = "Crear nuevo registro de personalización",
    description = "Crea un nuevo registro de personalización con los detalles de las opciones " +
            "elegidas por el cliente.")
    public ResponseEntity<PersonalizacionResponseDTO> crear(@RequestBody PersonalizacionCreateDTO dto) {
        try {
            PersonalizacionResponseDTO creada = personalizacionService.crear(dto);
            return ResponseEntity.ok(creada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // 400
        }
    }


    @PutMapping("/{id}")
    @Operation(summary = "Actualizar registro de personalización",
    description = "Modifica los datos de un registro de personalización " +
            "existente, identificado por su ID.")
    public ResponseEntity<PersonalizacionResponseDTO> actualizar(
            @PathVariable Integer id,
            @RequestBody PersonalizacionUpdateDTO dto) {

        PersonalizacionResponseDTO actualizada = personalizacionService.actualizar(id, dto);
        if (actualizada == null) {
            return ResponseEntity.notFound().build(); // 404
        }
        return ResponseEntity.ok(actualizada);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar registro de personalización",
    description = "Elimina permanentemente un registro de personalización del sistema.")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        boolean eliminado = personalizacionService.eliminar(id);
        if (!eliminado) {
            return ResponseEntity.notFound().build(); // 404
        }
        return ResponseEntity.noContent().build();
    }
}

package com.example.libreria_api.controller.experienciausuarios;

import com.example.libreria_api.dto.experienciausuarios.ContactoFormularioCreateDTO;
import com.example.libreria_api.dto.experienciausuarios.ContactoFormularioUpdateDTO;
import com.example.libreria_api.dto.experienciausuarios.ContactoFormularioResponseDTO;
import com.example.libreria_api.service.experienciausuarios.ContactoFormularioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/contactos")
public class ContactoFormularioController {

    private final ContactoFormularioService contactoService;

    public ContactoFormularioController(ContactoFormularioService contactoService) {
        this.contactoService = contactoService;
    }

    // ==============================
    // CREAR (público)
    // ==============================
    @PostMapping
    public ResponseEntity<ContactoFormularioResponseDTO> crearContacto(
            @Valid @RequestBody ContactoFormularioCreateDTO dto) {
        ContactoFormularioResponseDTO creado = contactoService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // ==============================
    // LISTAR (admin, con filtros)
    // ==============================
    @GetMapping
    public ResponseEntity<List<ContactoFormularioResponseDTO>> listarContactos(
            @RequestParam(required = false) String via,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Integer usuarioId,
            @RequestParam(required = false) String fechaDesde,
            @RequestParam(required = false) String fechaHasta) {

        LocalDateTime desde = (fechaDesde != null) ? LocalDateTime.parse(fechaDesde) : null;
        LocalDateTime hasta = (fechaHasta != null) ? LocalDateTime.parse(fechaHasta) : null;

        List<ContactoFormularioResponseDTO> contactos =
                contactoService.listarConFiltros(via, estado, usuarioId, desde, hasta);

        return ResponseEntity.ok(contactos);
    }

    // ==============================
    // OBTENER POR ID (admin)
    // ==============================
    @GetMapping("/{id}")
    public ResponseEntity<ContactoFormularioResponseDTO> obtenerPorId(@PathVariable Integer id) {
        ContactoFormularioResponseDTO contacto = contactoService.obtenerPorId(id);
        return ResponseEntity.ok(contacto);
    }

    // ==============================
    // ACTUALIZAR (admin)
    // ==============================
    @PutMapping("/{id}")
    public ResponseEntity<ContactoFormularioResponseDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ContactoFormularioUpdateDTO dto) {
        ContactoFormularioResponseDTO actualizado = contactoService.actualizar(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    // ==============================
    // ELIMINAR (admin)
    // ==============================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        contactoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> contarContactosPorEstado(@RequestParam String estado) {
        // Lo llamaremos desde PHP con /api/contactos/count?estado=pendiente
        return ResponseEntity.ok(contactoService.contarContactosPorEstado(estado));
    }
}

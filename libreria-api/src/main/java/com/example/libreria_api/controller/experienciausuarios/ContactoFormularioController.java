package com.example.libreria_api.controller.experienciausuarios;

import com.example.libreria_api.dto.experienciausuarios.ContactoFormularioCreateDTO;
import com.example.libreria_api.dto.experienciausuarios.ContactoFormularioUpdateDTO;
import com.example.libreria_api.dto.experienciausuarios.ContactoFormularioResponseDTO;
import com.example.libreria_api.service.experienciausuarios.ContactoFormularioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@Tag(name="Contactos(FORMULARIO)",description = "Gestión de los mensajes " +
        "recibidos a través del formulario de contacto público. Permite la creación (público), consulta, filtro y administración (admin) de estos registros.")
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
    @Operation(summary = "Enviar nuevo formulario de contacto",
    description = "Permite a un usuario público registrar un nuevo" +
            " mensaje o solicitud a través del formulario de contacto.")
    public ResponseEntity<ContactoFormularioResponseDTO> crearContacto(
            @Valid @RequestBody ContactoFormularioCreateDTO dto) {
        ContactoFormularioResponseDTO creado = contactoService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // ==============================
    // LISTAR (admin, con filtros)
    // ==============================
    @GetMapping
    @Operation(summary = "Listar y filtrar contactos",
    description = "Recupera una lista de contactos. Permite filtrar por via," +
            " estado, usuarioId y rangos de fecha (fechaDesde, fechaHasta).")
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
    @Operation(summary = "Obtener contacto por ID",
    description = "Recupera los detalles completos de un registro de contacto específico utilizando" +
            " su identificador único.")
    public ResponseEntity<ContactoFormularioResponseDTO> obtenerPorId(@PathVariable Integer id) {
        ContactoFormularioResponseDTO contacto = contactoService.obtenerPorId(id);
        return ResponseEntity.ok(contacto);
    }

    // ==============================
    // ACTUALIZAR (admin)
    // ==============================
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un contacto",
    description = "Modifica los detalles de un contacto existente " +
            "(generalmente el estado o la asignación a un usuarioId de administración).")
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
    @Operation(summary = "Eliminar un contacto",
    description = "Elimina permanentemente un registro de contacto del sistema.")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        contactoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    @Operation(summary = "Contar contactos por estado",
    description = "Devuelve el número de contactos que se encuentran en un estado específico")
    public ResponseEntity<Map<String, Long>> contarContactosPorEstado(@RequestParam String estado) {
        long count = contactoService.contarContactosPorEstado(estado);

        return ResponseEntity.ok(Map.of("count", count));
    }

    @GetMapping("/{id}/con-personalizacion")
    @Operation(summary = "Obtener contacto con detalles de personalización",
    description = "Recupera un registro de contacto específico junto con la información detallada de personalización" +
            "o preferencias asociadas.")
    public ResponseEntity<Map<String, Object>> obtenerContactoConPersonalizacion(@PathVariable Integer id) {
        Map<String, Object> resultado = contactoService.obtenerContactoConPersonalizacion(id);
        return ResponseEntity.ok(resultado);
    }
}

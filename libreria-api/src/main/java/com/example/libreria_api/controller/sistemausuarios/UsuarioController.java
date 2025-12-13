package com.example.libreria_api.controller.sistemausuarios;

import com.example.libreria_api.dto.sistemausuarios.*;
import com.example.libreria_api.service.sistemausuarios.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


import java.util.List;
import java.util.Map;

@RestController
@Tag(name="Usuarios",description = "Operaciones de gestión completa (CRUD) para" +
        "la entidad Usuario.")
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar y filtrar usuarios paginados",
    description = "Obtiene una lista paginada de todos los usuarios. Permite filtrar opcionalmente " +
            "por rolId y estado activo (true/false).")
    public Page<UsuarioResponseDTO> listarUsuarios(
            @RequestParam(required = false) Integer rolId,
            @RequestParam(required = false) Boolean activo, Pageable pageable) {
        return service.listarUsuarios(rolId, activo, pageable);
    }

    @GetMapping("/{usuId}")
    @Operation(summary = "Obtener usuario por ID",
    description = "Recupera los detalles completos de un usuario específico utilizando su " +
            "identificador único (usuId).")
    public UsuarioResponseDTO obtenerPorId(@PathVariable Integer usuId) {
        return service.obtenerPorId(usuId);
    }

    @PostMapping
    @Operation(summary = "Crear usuario nuevo",
    description = "Registra un nuevo usuario en el sistema. Requiere los datos básicos de creación.")
    public ResponseEntity<UsuarioResponseDTO> crear(@Valid @RequestBody UsuarioCreateDTO dto) {
        UsuarioResponseDTO creado = service.crear(dto);
        return ResponseEntity.status(201).body(creado);
    }


    @PutMapping("/{usuId}")
    @Operation(summary = "Actualizar datos completos del usuario",
            description = "Modifica todos los campos actualizables de un usuario existente identificado por " +
                    "su usuId.")
    public UsuarioResponseDTO actualizar(@PathVariable Integer usuId, @Valid @RequestBody UsuarioUpdateDTO dto) {
        return service.actualizar(usuId, dto);
    }

    @PatchMapping("/{usuId}/activo")
    @Operation(summary = "Actualizar estado activo/inactivo",
    description = "Cambia el estado de activación (activo) de un usuario específico. Útil para" +
            "suspensiones temporales.")
    public UsuarioResponseDTO actualizarActivo(@PathVariable Integer usuId, @RequestParam boolean activo) {
        UsuarioUpdateDTO dto = new UsuarioUpdateDTO();
        dto.setActivo(activo);
        return service.actualizar(usuId, dto);
    }

    @PatchMapping("/{usuId}/password")
    @Operation(summary = "Actualizar contraseña del usuario",
    description = "Modifica la contraseña de un usuario existente.")
    public ResponseEntity<Void> actualizarPassword(@PathVariable Integer usuId, @Valid @RequestBody PasswordUpdateDTO dto) {
        service.actualizarPassword(usuId, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{usuId}")
    @Operation(summary = "Eliminar usuario",
    description = "Elimina permanentemente a un usuario del sistema. Nota: Usualmente se prefiere " +
            "la desactivación (PATCH /activo).")
    public ResponseEntity<Void> eliminar(@PathVariable Integer usuId) {
        service.eliminar(usuId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{usuId}/rol")
    @Operation(summary = "Actualizar el rol de usuario",
    description = "Asigna un nuevo rol a un usuario existente identificado por su usuId.")
    public UsuarioResponseDTO actualizarRol(@PathVariable Integer usuId, @Valid @RequestBody RolUpdateDTO dto) {
        return service.actualizarRol(usuId, dto.getRolId());
    }

    @GetMapping("/empleados")
    public ResponseEntity<List<UsuarioResponseDTO>> obtenerEmpleadosPorRoles() {
        // Definimos los roles asignables: 2 (administrador) y 3 (diseñador).
        List<Integer> rolesAsignables = List.of(2, 3);

        // 🔥 CORRECCIÓN: Usar la variable inyectada 'service'
        List<UsuarioResponseDTO> empleados = service.obtenerUsuariosPorRoles(rolesAsignables);

        return ResponseEntity.ok(empleados);
    }

    // --- ENDPOINT CORREGIDO ---
    // El @RequestParam ya estaba bien, pero lo hago más explícito
    @GetMapping("/count")
    @Operation(summary = "Contar número de usuarios",
    description = "Devuelve el conteo total de usuarios. Permite contar opcionalmente" +
            " solo los usuarios activos o inactivos.")
    public ResponseEntity<Map<String, Long>> contarUsuarios(
            @RequestParam(required = false) Boolean activo
    ) {
        long count;

        if (activo != null) {
            count = service.contarUsuariosPorActivo(activo);
        } else {
            count = service.contarUsuariosPorActivo(true) +
                    service.contarUsuariosPorActivo(false);
        }

        return ResponseEntity.ok(Map.of("count", count));
    }
}
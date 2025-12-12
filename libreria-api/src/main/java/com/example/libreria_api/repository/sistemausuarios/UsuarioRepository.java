package com.example.libreria_api.repository.sistemausuarios;

import com.example.libreria_api.model.sistemausuarios.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Buscar por correo (para login o validación de duplicado)
    Optional<Usuario> findByUsuCorreo(String usuCorreo);

    // Validar existencia por correo y documento (para 409 Conflict)
    boolean existsByUsuCorreo(String usuCorreo);
    boolean existsByUsuDocnum(String usuDocnum);

    // Filtro por rol con paginación
    Page<Usuario> findByRol_RolId(Integer rolId, Pageable pageable);

    // Filtro por activo con paginación
    Page<Usuario> findByUsuActivo(Boolean usuActivo, Pageable pageable);

    // Filtro combinado (rol y activo)
    Page<Usuario> findByRol_RolIdAndUsuActivo(Integer rolId, Boolean usuActivo, Pageable pageable);
    // Buscar usuarios cuyos IDs de Rol estén DENTRO de la lista proporcionada
    List<Usuario> findByRol_RolIdInAndUsuActivo(List<Integer> rolIds, Boolean activo);

    long countByUsuActivo(boolean activo);
}


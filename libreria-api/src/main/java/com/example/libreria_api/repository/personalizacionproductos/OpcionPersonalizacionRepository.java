package com.example.libreria_api.repository.personalizacionproductos;

import com.example.libreria_api.model.personalizacionproductos.OpcionPersonalizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OpcionPersonalizacionRepository extends JpaRepository<OpcionPersonalizacion, Integer> {

    // Buscar por nombre exacto (usado para validación de unicidad)
    Optional<OpcionPersonalizacion> findByOpcNombre(String opcNombre);

    // Búsqueda insensible a mayúsculas/minúsculas (ej. "gema" ~ "Gema")
    List<OpcionPersonalizacion> findByOpcNombreContainingIgnoreCase(String nombre);
}

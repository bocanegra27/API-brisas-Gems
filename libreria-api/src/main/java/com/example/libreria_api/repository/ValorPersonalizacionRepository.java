package com.example.libreria_api.repository;

import com.example.libreria_api.model.ValorPersonalizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ValorPersonalizacionRepository extends JpaRepository<ValorPersonalizacion, Integer> {

    // Buscar todos los valores asociados a una opción
    List<ValorPersonalizacion> findByOpcionPersonalizacion_OpcId(Integer opcId);

    // Buscar por nombre (contiene, ignorando mayúsculas/minúsculas)
    List<ValorPersonalizacion> findByValNombreContainingIgnoreCase(String nombre);

    // Buscar por opción + nombre (para validaciones de duplicados o filtro conjunto)
    List<ValorPersonalizacion> findByOpcionPersonalizacion_OpcIdAndValNombreContainingIgnoreCase(
            Integer opcId,
            String nombre
    );
}
package com.example.libreria_api.repository.personalizacionproductos;

import com.example.libreria_api.model.personalizacionproductos.DetallePersonalizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetallePersonalizacionRepository extends JpaRepository<DetallePersonalizacion, Integer> {

    // Buscar todos los detalles de una personalización
    List<DetallePersonalizacion> findByPersonalizacion_PerId(Integer perId);

    // Buscar detalles filtrando por valor seleccionado
    List<DetallePersonalizacion> findByValorPersonalizacion_ValId(Integer valId);

    // Buscar por combinación (útil para evitar duplicados)
    List<DetallePersonalizacion> findByPersonalizacion_PerIdAndValorPersonalizacion_ValId(Integer perId, Integer valId);
}

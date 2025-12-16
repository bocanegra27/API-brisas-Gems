package com.example.libreria_api.repository.personalizacionproductos;

import com.example.libreria_api.model.personalizacionproductos.DetallePersonalizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetallePersonalizacionRepository extends JpaRepository<DetallePersonalizacion, Integer> {


    List<DetallePersonalizacion> findByPersonalizacion_PerId(Integer perId);


    List<DetallePersonalizacion> findByValorPersonalizacion_ValId(Integer valId);


    List<DetallePersonalizacion> findByPersonalizacion_PerIdAndValorPersonalizacion_ValId(Integer perId, Integer valId);
}

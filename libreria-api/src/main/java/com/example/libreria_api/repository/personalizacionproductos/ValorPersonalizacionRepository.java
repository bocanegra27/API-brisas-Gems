package com.example.libreria_api.repository.personalizacionproductos;

import com.example.libreria_api.model.personalizacionproductos.ValorPersonalizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ValorPersonalizacionRepository extends JpaRepository<ValorPersonalizacion, Integer> {


    List<ValorPersonalizacion> findByOpcionPersonalizacion_OpcId(Integer opcId);


    List<ValorPersonalizacion> findByValNombreContainingIgnoreCase(String nombre);


    List<ValorPersonalizacion> findByOpcionPersonalizacion_OpcIdAndValNombreContainingIgnoreCase(
            Integer opcId,
            String nombre
    );
}
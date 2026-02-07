package com.example.libreria_api.repository.personalizacionproductos;

import com.example.libreria_api.model.personalizacionproductos.OpcionPersonalizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OpcionPersonalizacionRepository extends JpaRepository<OpcionPersonalizacion, Integer> {


    Optional<OpcionPersonalizacion> findByOpcNombre(String opcNombre);


    List<OpcionPersonalizacion> findByOpcNombreContainingIgnoreCase(String nombre);
}

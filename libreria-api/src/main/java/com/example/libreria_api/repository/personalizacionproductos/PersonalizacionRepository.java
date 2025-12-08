package com.example.libreria_api.repository.personalizacionproductos;

import com.example.libreria_api.model.personalizacionproductos.Personalizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PersonalizacionRepository extends JpaRepository<Personalizacion, Integer> {

    List<Personalizacion> findByUsuario_UsuId(Integer usuarioId);

    List<Personalizacion> findBySesion_SesId(Integer sesionId);

    List<Personalizacion> findByPerFechaBetween(LocalDate desde, LocalDate hasta);

    List<Personalizacion> findByUsuario_UsuIdAndPerFechaBetween(Integer usuarioId, LocalDate desde, LocalDate hasta);

    @Query("SELECT p FROM Personalizacion p WHERE p.sesion.sesId = :sesionId AND p.usuario IS NULL")
    List<Personalizacion> findPersonalizacionesAnonimasPorSesion(Integer sesionId);
}
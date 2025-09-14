package com.example.libreria_api.repository;

import com.example.libreria_api.model.Personalizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PersonalizacionRepository extends JpaRepository<Personalizacion, Integer> {

    List<Personalizacion> findByUsuario_UsuId(int usuarioId);

    List<Personalizacion> findByPerFechaBetween(LocalDate desde, LocalDate hasta);

    List<Personalizacion> findByUsuario_UsuIdAndPerFechaBetween(int usuarioId, LocalDate desde, LocalDate hasta);
}

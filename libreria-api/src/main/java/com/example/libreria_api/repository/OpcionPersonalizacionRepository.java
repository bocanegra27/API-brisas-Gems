package com.example.libreria_api.repository;

import com.example.libreria_api.model.OpcionPersonalizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpcionPersonalizacionRepository extends JpaRepository<OpcionPersonalizacion, Integer> {
}
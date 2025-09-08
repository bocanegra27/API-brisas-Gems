package com.example.libreria_api.repository;

import com.example.libreria_api.model.ValorPersonalizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValorPersonalizacionRepository extends JpaRepository<ValorPersonalizacion, Integer> {
}
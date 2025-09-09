package com.example.libreria_api.repository;

import com.example.libreria_api.model.Personalizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalizacionRepository extends JpaRepository<Personalizacion, Integer> {
}

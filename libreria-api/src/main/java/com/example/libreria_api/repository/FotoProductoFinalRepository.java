package com.example.libreria_api.repository;

import com.example.libreria_api.model.FotoProductoFinal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FotoProductoFinalRepository extends JpaRepository<FotoProductoFinal, Integer> {
}
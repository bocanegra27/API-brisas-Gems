package com.example.libreria_api.repository;

import com.example.libreria_api.model.PortafolioInspiracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortafolioInspiracionRepository extends JpaRepository<PortafolioInspiracion, Integer> {

}

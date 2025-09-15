package com.example.libreria_api.repository.experienciausuarios;

import com.example.libreria_api.model.experienciausuarios.PortafolioInspiracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortafolioInspiracionRepository extends JpaRepository<PortafolioInspiracion, Integer> {

}

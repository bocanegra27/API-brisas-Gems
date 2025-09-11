package com.example.libreria_api.repository.sistemausuarios;

import com.example.libreria_api.model.sistemausuarios.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {

}
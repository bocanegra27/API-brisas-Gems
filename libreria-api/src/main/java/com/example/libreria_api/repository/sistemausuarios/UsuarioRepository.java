package com.example.libreria_api.repository.sistemausuarios;

import com.example.libreria_api.model.sistemausuarios.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
}
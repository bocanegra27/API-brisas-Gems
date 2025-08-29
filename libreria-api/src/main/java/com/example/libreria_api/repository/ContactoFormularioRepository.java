package com.example.libreria_api.repository;

import com.example.libreria_api.model.ContactoFormulario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactoFormularioRepository extends JpaRepository<ContactoFormulario, Integer> {

}

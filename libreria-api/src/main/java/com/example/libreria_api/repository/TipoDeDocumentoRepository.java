package com.example.libreria_api.repository;

import com.example.libreria_api.model.TipoDeDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoDeDocumentoRepository extends JpaRepository<TipoDeDocumento, Integer> {
}
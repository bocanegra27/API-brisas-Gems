package com.example.libreria_api.repository.sistemausuarios;

import com.example.libreria_api.model.sistemausuarios.TipoDeDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoDeDocumentoRepository extends JpaRepository<TipoDeDocumento, Integer> {
}
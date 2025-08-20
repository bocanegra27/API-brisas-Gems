package com.example.libreria_api.service;

import com.example.libreria_api.model.TipoDeDocumento;
import com.example.libreria_api.repository.TipoDeDocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TipoDeDocumentoService {

    @Autowired
    private TipoDeDocumentoRepository tipoDeDocumentoRepository;

    public List<TipoDeDocumento> obtenerTodos() {
        return tipoDeDocumentoRepository.findAll();
    }

    public TipoDeDocumento guardar(TipoDeDocumento tipoDeDocumento) {
        return tipoDeDocumentoRepository.save(tipoDeDocumento);
    }
}
package com.example.libreria_api.service.sistemausuarios;

import com.example.libreria_api.model.sistemausuarios.TipoDeDocumento;
import com.example.libreria_api.repository.sistemausuarios.TipoDeDocumentoRepository;
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

    public TipoDeDocumento actualizar(Integer id, TipoDeDocumento detalles) {
        return tipoDeDocumentoRepository.findById(id).map(tipoDeDocExistente -> {
            tipoDeDocExistente.setTipdocNombre(detalles.getTipdocNombre());

            return tipoDeDocumentoRepository.save(tipoDeDocExistente);
        }).orElse(null);
    }

    public void eliminardocumento(Integer id) {

        tipoDeDocumentoRepository.deleteById(id);
    }
}
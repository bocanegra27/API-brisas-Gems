package com.example.libreria_api.service;

import com.example.libreria_api.model.OpcionPersonalizacion;
import com.example.libreria_api.repository.OpcionPersonalizacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OpcionPersonalizacionService {

    @Autowired
    private OpcionPersonalizacionRepository opcionPersonalizacionRepository;

    public List<OpcionPersonalizacion> obtenerTodos() {
        return opcionPersonalizacionRepository.findAll();
    }

    public OpcionPersonalizacion guardar(OpcionPersonalizacion opcionPersonalizacion) {
        return opcionPersonalizacionRepository.save(opcionPersonalizacion);
    }

    public OpcionPersonalizacion actualizar(Integer id, OpcionPersonalizacion detalles) {
        return opcionPersonalizacionRepository.findById(id).map(opcionExistente -> {
            opcionExistente.setOpcNombre(detalles.getOpcNombre());
            return opcionPersonalizacionRepository.save(opcionExistente);
        }).orElse(null);
    }
}
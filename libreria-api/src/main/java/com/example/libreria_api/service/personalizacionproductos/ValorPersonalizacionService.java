package com.example.libreria_api.service.personalizacionproductos;

import com.example.libreria_api.model.personalizacionproductos.ValorPersonalizacion;
import com.example.libreria_api.repository.personalizacionproductos.ValorPersonalizacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ValorPersonalizacionService {

    @Autowired
    private ValorPersonalizacionRepository valorPersonalizacionRepository;


    public List<ValorPersonalizacion> obtenerTodos() {
        return valorPersonalizacionRepository.findAll();
    }


    public ValorPersonalizacion guardar(ValorPersonalizacion valorPersonalizacion) {
        return valorPersonalizacionRepository.save(valorPersonalizacion);
    }

    public Optional<ValorPersonalizacion> obtenerPorId(Integer id) {
        return valorPersonalizacionRepository.findById(id);
    }


    public ValorPersonalizacion actualizar(Integer id, ValorPersonalizacion detalles) {
        return valorPersonalizacionRepository.findById(id).map(existente -> {
            existente.setValNombre(detalles.getValNombre());
            existente.setValImagen(detalles.getValImagen());
            existente.setOpcionPersonalizacion(detalles.getOpcionPersonalizacion());
            return valorPersonalizacionRepository.save(existente);
        }).orElse(null);
    }


    public boolean eliminar(Integer id) {
        return valorPersonalizacionRepository.findById(id).map(existente -> {
            valorPersonalizacionRepository.delete(existente);
            return true;
        }).orElse(false);
    }
}

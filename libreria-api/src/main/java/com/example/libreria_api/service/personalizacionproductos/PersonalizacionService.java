package com.example.libreria_api.service.personalizacionproductos;

import com.example.libreria_api.model.personalizacionproductos.Personalizacion;
import com.example.libreria_api.repository.personalizacionproductos.PersonalizacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PersonalizacionService {

    @Autowired
    private  PersonalizacionRepository personalizacionRepository;

    public List<Personalizacion> obtenerPersonalizaciones() {
        return personalizacionRepository.findAll();
    }

    public Personalizacion crearPersonalizacion(Personalizacion personalizacion) {
        return personalizacionRepository.save(personalizacion);
    }

    public Personalizacion actualizarPersonalizacion(Integer id, Personalizacion personalizacion) {
        return personalizacionRepository.findById(id).map(personalizacionExistente -> {
            personalizacionExistente.setPerFecha(personalizacion.getPerFecha());
            personalizacionExistente.setUsuId(personalizacion.getUsuId());

            return personalizacionRepository.save(personalizacionExistente);
        }).orElse(null);
    }

    public void eliminarPersonalizacion (Integer id){
        personalizacionRepository.deleteById(id);
    }
}


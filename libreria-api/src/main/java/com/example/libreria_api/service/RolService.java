package com.example.libreria_api.service;

import com.example.libreria_api.model.Rol;
import com.example.libreria_api.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RolService {


    @Autowired
    private RolRepository rolRepository;

    public List<Rol> obtenerTodosLosRoles() {
        return rolRepository.findAll();
    }


    public Rol guardarRol(Rol rol) {
        return rolRepository.save(rol);
    }

}
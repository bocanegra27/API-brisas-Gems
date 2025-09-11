package com.example.libreria_api.service.sistemausuarios;

import com.example.libreria_api.model.sistemausuarios.Rol;
import com.example.libreria_api.repository.sistemausuarios.RolRepository;
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

    public Rol actualizar(Integer id, Rol detalles) {
        return rolRepository.findById(id).map(rolExistente -> {
            rolExistente.setRolNombre(detalles.getRolNombre());

            return rolRepository.save(rolExistente);
        }).orElse(null);
    }

    public void eliminarRol (Integer id){
        rolRepository.deleteById(id);
    }

}
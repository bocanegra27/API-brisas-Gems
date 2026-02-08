package com.example.libreria_api.service.personalizacionproductos;

import com.example.libreria_api.dto.personalizacionproductos.CategoriaProductoCreateDTO;
import com.example.libreria_api.dto.personalizacionproductos.CategoriaProductoResponseDTO;
import com.example.libreria_api.model.personalizacionproductos.CategoriaProducto;
import com.example.libreria_api.repository.personalizacionproductos.CategoriaProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaProductoService {

    private final CategoriaProductoRepository categoriaRepo;

    public CategoriaProductoService(CategoriaProductoRepository categoriaRepo) {
        this.categoriaRepo = categoriaRepo;
    }

    @Transactional(readOnly = true)
    public List<CategoriaProductoResponseDTO> listar() {
        return categoriaRepo.findAll().stream()
                .map(c -> new CategoriaProductoResponseDTO(c.getCatId(), c.getCatNombre(), c.getCatSlug()))
                .collect(Collectors.toList());
    }

    // ESTE ES EL MÉTDO QUE TE FALTA
    @Transactional
    public CategoriaProductoResponseDTO crear(CategoriaProductoCreateDTO dto) {
        // Validar que el slug no exista (opcional pero recomendado)
        // Podrías agregar un método en el repo: existsByCatSlug(String slug)

        CategoriaProducto nueva = new CategoriaProducto();
        nueva.setCatNombre(dto.getNombre());
        nueva.setCatSlug(dto.getSlug());

        try {
            CategoriaProducto guardada = categoriaRepo.save(nueva);
            return new CategoriaProductoResponseDTO(guardada.getCatId(), guardada.getCatNombre(), guardada.getCatSlug());
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Ya existe una categoría con ese nombre o slug.");
        }
    }

    @Transactional
    public void eliminar(Integer id) {
        if (!categoriaRepo.existsById(id)) {
            throw new EntityNotFoundException("Categoría no encontrada");
        }
        // Al borrar la categoría, JPA borrará las opciones y valores por el CascadeType.ALL
        categoriaRepo.deleteById(id);
    }


}
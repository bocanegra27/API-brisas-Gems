package com.example.libreria_api.service.personalizacionproductos;

import com.example.libreria_api.dto.personalizacionproductos.OpcionPersonalizacionCreateDTO;
import com.example.libreria_api.dto.personalizacionproductos.OpcionPersonalizacionResponseDTO;
import com.example.libreria_api.dto.personalizacionproductos.OpcionPersonalizacionUpdateDTO;
import com.example.libreria_api.model.personalizacionproductos.CategoriaProducto;
import com.example.libreria_api.model.personalizacionproductos.OpcionPersonalizacion;
import com.example.libreria_api.model.personalizacionproductos.ValorPersonalizacion;
import com.example.libreria_api.repository.personalizacionproductos.CategoriaProductoRepository;
import com.example.libreria_api.repository.personalizacionproductos.OpcionPersonalizacionRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OpcionPersonalizacionService {

    private final OpcionPersonalizacionRepository opcionRepo;
    private final CategoriaProductoRepository categoriaRepo; // Repositorio de categorías

    public OpcionPersonalizacionService(OpcionPersonalizacionRepository opcionRepo,
                                        CategoriaProductoRepository categoriaRepo) {
        this.opcionRepo = opcionRepo;
        this.categoriaRepo = categoriaRepo;
    }

    @Transactional
    public OpcionPersonalizacionResponseDTO crear(OpcionPersonalizacionCreateDTO dto) {
        // 1. Validar nombre único
        if (opcionRepo.findByOpcNombre(dto.getNombre()).isPresent()) {
            throw new DataIntegrityViolationException("Ya existe una opción con ese nombre");
        }

        // 2. Buscar la categoría obligatoria
        CategoriaProducto categoria = categoriaRepo.findById(dto.getCatId())
                .orElseThrow(() -> new EntityNotFoundException("La categoría con ID " + dto.getCatId() + " no existe"));

        // 3. Crear y vincular
        OpcionPersonalizacion nueva = new OpcionPersonalizacion();
        nueva.setOpcNombre(dto.getNombre());
        nueva.setCategoria(categoria); // <--- VINCULACIÓN CRÍTICA

        OpcionPersonalizacion guardada = opcionRepo.save(nueva);

        // 4. Retornar DTO con los 3 datos
        return new OpcionPersonalizacionResponseDTO(
                guardada.getOpcId(),
                guardada.getOpcNombre(),
                guardada.getCategoria().getCatId() // <--- TERCER PARÁMETRO
        );
    }

    @Transactional
    public OpcionPersonalizacionResponseDTO actualizar(int id, OpcionPersonalizacionUpdateDTO dto) {
        OpcionPersonalizacion opcion = opcionRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Opción no encontrada con id: " + id));

        // Validar nombre duplicado
        Optional<OpcionPersonalizacion> existente = opcionRepo.findByOpcNombre(dto.getNombre());
        if (existente.isPresent() && existente.get().getOpcId() != id) {
            throw new DataIntegrityViolationException("Ya existe otra opción con ese nombre");
        }

        opcion.setOpcNombre(dto.getNombre());
        // Nota: Por ahora no actualizamos la categoría en el update, pero se podría agregar si quisieras.

        OpcionPersonalizacion actualizada = opcionRepo.save(opcion);

        return new OpcionPersonalizacionResponseDTO(
                actualizada.getOpcId(),
                actualizada.getOpcNombre(),
                actualizada.getCategoria().getCatId()
        );
    }

    // MODIFICADO: Acepta catId opcional
    public List<OpcionPersonalizacionResponseDTO> listar(String search, Integer catId) {
        List<OpcionPersonalizacion> opciones;

        if (catId != null) {
            // Filtramos por categoría usando el repositorio correcto (opcionRepo)
            opciones = opcionRepo.findByCategoria_CatId(catId);
        } else if (search != null && !search.isEmpty()) {
            opciones = opcionRepo.findByOpcNombreContainingIgnoreCase(search);
        } else {
            opciones = opcionRepo.findAll();
        }

        // Mapear a DTO incluyendo el catId
        return opciones.stream()
                .map(opc -> new OpcionPersonalizacionResponseDTO(
                        opc.getOpcId(),
                        opc.getOpcNombre(),
                        opc.getCategoria().getCatId()
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OpcionPersonalizacionResponseDTO obtenerPorId(int id) {
        OpcionPersonalizacion opcion = opcionRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Opción no encontrada con id: " + id));

        return new OpcionPersonalizacionResponseDTO(
                opcion.getOpcId(),
                opcion.getOpcNombre(),
                opcion.getCategoria().getCatId()
        );
    }

    @Transactional
    public void eliminar(int id) {
        OpcionPersonalizacion opcion = opcionRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Opción no encontrada con id: " + id));

        // Al borrar la opción, Hibernate borrará automáticamente sus valores (cascade)
        opcionRepo.delete(opcion);
    }
}
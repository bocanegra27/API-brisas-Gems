package com.example.libreria_api.repository.personalizacionproductos;

import com.example.libreria_api.model.personalizacionproductos.CategoriaProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaProductoRepository extends JpaRepository<CategoriaProducto, Integer> {
    // Puedes agregar buscar por slug si lo necesitas luego
}
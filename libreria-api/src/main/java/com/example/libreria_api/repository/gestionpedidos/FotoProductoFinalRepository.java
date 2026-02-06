package com.example.libreria_api.repository.gestionpedidos;

import com.example.libreria_api.model.gestionpedidos.FotoProductoFinal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FotoProductoFinalRepository extends JpaRepository<FotoProductoFinal, Integer> {

    // Usamos una consulta JPQL explícita para evitar errores de nombres
    @Query("SELECT f FROM FotoProductoFinal f WHERE f.pedido.ped_id = :pedId")
    List<FotoProductoFinal> buscarPorPedidoId(@Param("pedId") Integer pedId);
}
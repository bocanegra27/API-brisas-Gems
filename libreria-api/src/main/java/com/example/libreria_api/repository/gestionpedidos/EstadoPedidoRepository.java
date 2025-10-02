package com.example.libreria_api.repository.gestionpedidos;

import com.example.libreria_api.model.gestionpedidos.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstadoPedidoRepository extends JpaRepository<EstadoPedido, Integer> {

    // Buscar estado por nombre exacto
    Optional<EstadoPedido> findByEstNombre(String estNombre);

    // Verificar si existe un estado por nombre
    boolean existsByEstNombre(String estNombre);

    // Buscar estado por nombre (case insensitive)
    @Query("SELECT e FROM EstadoPedido e WHERE LOWER(e.estNombre) = LOWER(:nombre)")
    Optional<EstadoPedido> findByEstNombreIgnoreCase(@Param("nombre") String nombre);

    // Contar pedidos por estado
    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.estadoPedido.est_id = :estadoId")
    Long countPedidosByEstadoId(@Param("estadoId") Integer estadoId);
}
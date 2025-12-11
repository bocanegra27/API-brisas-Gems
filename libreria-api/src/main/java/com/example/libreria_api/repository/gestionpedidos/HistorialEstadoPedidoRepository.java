package com.example.libreria_api.repository.gestionpedidos;

import com.example.libreria_api.model.gestionpedidos.HistorialEstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialEstadoPedidoRepository extends JpaRepository<HistorialEstadoPedido, Integer> {

    // Método para obtener el historial completo de un pedido, ordenado por fecha descendente (timeline)
    @Query("SELECT h FROM HistorialEstadoPedido h " +
            "LEFT JOIN FETCH h.estadoPedido ep " +
            "LEFT JOIN FETCH h.usuarioResponsable ur " +
            // Usar el nombre exacto del campo ID en la Entidad Pedido.java
            "WHERE h.pedido.ped_id = :pedidoId " +
            "ORDER BY h.hisFechaCambio DESC")
    List<HistorialEstadoPedido> findHistorialConDetalles(@Param("pedidoId") Integer pedidoId);
}
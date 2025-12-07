package com.example.libreria_api.repository.gestionpedidos;

import com.example.libreria_api.model.gestionpedidos.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    long countByEstadoPedido_EstNombre(String nombreEstado);
    @Query("SELECT p FROM Pedido p JOIN FETCH p.estadoPedido ORDER BY p.ped_id DESC")
    List<Pedido> findAllWithEstadoEagerly();

    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.estadoPedido.est_id = :estadoId")
    long countByEstadoId(@Param("estadoId") Integer estadoId);
}


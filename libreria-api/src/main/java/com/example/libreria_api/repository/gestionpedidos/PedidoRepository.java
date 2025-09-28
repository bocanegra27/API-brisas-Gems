package com.example.libreria_api.repository.gestionpedidos;

import com.example.libreria_api.model.gestionpedidos.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    long countByEstadoPedido_EstNombre(String nombreEstado);

}
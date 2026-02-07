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

    @Query("SELECT p FROM Pedido p WHERE p.sesion.sesId = :sesionId")
    List<Pedido> findBySesionId(@Param("sesionId") Integer sesionId);


    @Query("SELECT p FROM Pedido p JOIN FETCH p.estadoPedido WHERE p.cliente.usuId = :usuIdCliente ORDER BY p.ped_id DESC")
    List<Pedido> findByClienteUsuId(@Param("usuIdCliente") Integer usuIdCliente);


    @Query("SELECT p FROM Pedido p JOIN FETCH p.estadoPedido WHERE p.empleadoAsignado.usuId = :usuIdEmpleado ORDER BY p.ped_id DESC")
    List<Pedido> findByEmpleadoAsignadoUsuId(@Param("usuIdEmpleado") Integer usuIdEmpleado);
}


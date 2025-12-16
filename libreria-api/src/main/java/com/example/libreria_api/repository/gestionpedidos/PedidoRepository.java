package com.example.libreria_api.repository.gestionpedidos;

import com.example.libreria_api.model.gestionpedidos.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    // Contar pedidos por nombre de estado
    long countByEstadoPedido_EstNombre(String nombreEstado);

    // Obtener todos los pedidos cargando el estado (Eager Loading)
    // Usamos LEFT JOIN FETCH para traer pedidos incluso si el estado es nulo
    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.estadoPedido ORDER BY p.ped_id DESC")
    List<Pedido> findAllWithEstadoEagerly();

    // Contar pedidos por ID de estado
    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.estadoPedido.est_id = :estadoId")
    long countByEstadoId(@Param("estadoId") Integer estadoId);

    // Buscar pedidos por sesión anónima
    @Query("SELECT p FROM Pedido p WHERE p.sesion.sesId = :sesionId")
    List<Pedido> findBySesionId(@Param("sesionId") Integer sesionId);

    // ========================================================================
    // 🔥 DASHBOARD DE CLIENTE (CORREGIDO)
    // ========================================================================
    // Usamos LEFT JOIN FETCH para asegurar que el cliente vea sus pedidos
    // aunque tengan inconsistencias en la tabla de estados.
    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.estadoPedido WHERE p.cliente.usuId = :usuIdCliente ORDER BY p.ped_id DESC")
    List<Pedido> findByClienteUsuId(@Param("usuIdCliente") Integer usuIdCliente);

    // ========================================================================
    // 🔥 DASHBOARD DE EMPLEADO/DISEÑADOR (CORREGIDO)
    // ========================================================================
    // Usamos LEFT JOIN FETCH para asegurar que el empleado vea sus asignaciones
    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.estadoPedido WHERE p.empleadoAsignado.usuId = :usuIdEmpleado ORDER BY p.ped_id DESC")
    List<Pedido> findByEmpleadoAsignadoUsuId(@Param("usuIdEmpleado") Integer usuIdEmpleado);
}
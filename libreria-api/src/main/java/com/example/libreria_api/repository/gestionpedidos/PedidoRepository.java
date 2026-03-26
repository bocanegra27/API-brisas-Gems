package com.example.libreria_api.repository.gestionpedidos;

import com.example.libreria_api.dto.gestionpedidos.ReporteDiseñadorDTO;
import com.example.libreria_api.dto.gestionpedidos.ReporteEstadoDTO;
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

    @Query("SELECT p FROM Pedido p " +
            "JOIN FETCH p.estadoPedido " +
            "LEFT JOIN FETCH p.cliente " +
            "LEFT JOIN FETCH p.empleadoAsignado " +
            "WHERE (:estadoId IS NULL OR p.estadoPedido.est_id = :estadoId) " +
            "AND (:codigo IS NULL OR LOWER(p.pedCodigo) LIKE LOWER(CONCAT('%', :codigo, '%'))) " +
            "AND (:usuIdCliente IS NULL OR p.cliente.usuId = :usuIdCliente) " +
            "AND (:usuIdEmpleado IS NULL OR p.empleadoAsignado.usuId = :usuIdEmpleado) " +
            "ORDER BY p.ped_id DESC")
    List<Pedido> findAllFiltrados(
            @Param("estadoId") Integer estadoId,
            @Param("codigo") String codigo,
            @Param("usuIdCliente") Integer usuIdCliente,
            @Param("usuIdEmpleado") Integer usuIdEmpleado
    );

    // Reportes: conteo de pedidos agrupados por estado (todos los estados)
    @Query("SELECT new com.example.libreria_api.dto.gestionpedidos.ReporteEstadoDTO(" +
            "e.est_id, e.estNombre, COUNT(p)) " +
            "FROM EstadoPedido e LEFT JOIN Pedido p ON p.estadoPedido.est_id = e.est_id " +
            "GROUP BY e.est_id, e.estNombre ORDER BY e.est_id ASC")
    List<ReporteEstadoDTO> findResumenPorEstado();

    // Reportes: pedidos agrupados por diseñador asignado
    @Query("SELECT new com.example.libreria_api.dto.gestionpedidos.ReporteDiseñadorDTO(" +
            "u.usuId, u.usuNombre, COUNT(p)) " +
            "FROM Usuario u JOIN Pedido p ON p.empleadoAsignado.usuId = u.usuId " +
            "WHERE u.rol.rolId = 3 " +
            "GROUP BY u.usuId, u.usuNombre ORDER BY COUNT(p) DESC")
    List<ReporteDiseñadorDTO> findResumenPorDiseñador();

    // Reportes: pedidos en estado >= 3 que no tienen render registrado
    @Query("SELECT p FROM Pedido p " +
            "JOIN FETCH p.estadoPedido " +
            "LEFT JOIN FETCH p.cliente " +
            "LEFT JOIN FETCH p.empleadoAsignado " +
            "WHERE p.estadoPedido.est_id >= 3 " +
            "AND p.ped_id NOT IN (SELECT r.pedido.ped_id FROM Render3d r) " +
            "ORDER BY p.estadoPedido.est_id ASC")
    List<Pedido> findPedidosSinRender();
}


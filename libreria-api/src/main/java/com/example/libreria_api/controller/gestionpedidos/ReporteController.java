package com.example.libreria_api.controller.gestionpedidos;

import com.example.libreria_api.dto.gestionpedidos.PedidoResponseDTO;
import com.example.libreria_api.dto.gestionpedidos.ReporteDiseñadorDTO;
import com.example.libreria_api.dto.gestionpedidos.ReporteEstadoDTO;
import com.example.libreria_api.service.gestionpedidos.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
@Tag(name = "Reportes", description = "Metricas y resumenes operativos del sistema para el administrador.")
public class ReporteController {

    private final PedidoService pedidoService;

    public ReporteController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/pedidos-por-estado")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(
            summary = "Resumen de pedidos agrupados por estado",
            description = "Devuelve todos los estados del sistema con el conteo de pedidos en cada uno. " +
                    "Estados sin pedidos aparecen con total = 0."
    )
    public ResponseEntity<List<ReporteEstadoDTO>> pedidosPorEstado() {
        return ResponseEntity.ok(pedidoService.obtenerResumenPorEstado());
    }

    @GetMapping("/pedidos-por-disenador")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(
            summary = "Resumen de pedidos asignados por diseñador",
            description = "Devuelve cada diseñador con el total de pedidos que tiene asignados actualmente."
    )
    public ResponseEntity<List<ReporteDiseñadorDTO>> pedidosPorDiseñador() {
        return ResponseEntity.ok(pedidoService.obtenerResumenPorDiseñador());
    }

    @GetMapping("/pedidos-sin-render")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(
            summary = "Pedidos en produccion sin render subido",
            description = "Lista pedidos que ya estan en estado Diseno en Proceso o superior " +
                    "pero aun no tienen ningun archivo render registrado."
    )
    public ResponseEntity<List<PedidoResponseDTO>> pedidosSinRender() {
        return ResponseEntity.ok(pedidoService.obtenerPedidosSinRender());
    }

    @GetMapping("/contactos-sin-convertir")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(
            summary = "Contactos pendientes sin conversion a pedido",
            description = "Cuenta los formularios de contacto en estado pendiente que no han " +
                    "sido convertidos a pedido activo. Son oportunidades comerciales abiertas."
    )
    public ResponseEntity<Map<String, Long>> contactosSinConvertir() {
        Long total = pedidoService.obtenerContactosPendientesSinPedido();
        return ResponseEntity.ok(Map.of("contactosPendientes", total));
    }
}
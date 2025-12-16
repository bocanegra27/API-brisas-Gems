package com.example.libreria_api.dto.gestionpedidos;

import com.example.libreria_api.model.gestionpedidos.Pedido;

public class PedidoMapper {

    public static PedidoResponseDTO toPedidoResponseDTO(Pedido pedido) {
        if (pedido == null) return null;

        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setPedId(pedido.getPed_id());
        dto.setPedCodigo(pedido.getPedCodigo());
        dto.setPedFechaCreacion(pedido.getPedFechaCreacion());
        dto.setPedComentarios(pedido.getPedComentarios());


        if (pedido.getEstadoPedido() != null) {
            dto.setEstId(pedido.getEstadoPedido().getEst_id());
            dto.setEstadoNombre(pedido.getEstadoPedido().getEstNombre());
        }

        // --- CORRECCIÓN AQUÍ ---
        // Inicializamos el costo en 0.0 para evitar errores si no encontramos el dato
        dto.setPedCostoTotal(0.0);

        // Trazabilidad 1: Personalización
        if (pedido.getPersonalizacion() != null) {
            dto.setPerId(pedido.getPersonalizacion().getPerId());

            // NOTA: Como 'Personalizacion' no tiene precio, aquí deberíamos
            // sumar los detalles o sacar el precio de la tabla Pedido si existe.
            // Por ahora lo dejamos en 0.0 para que el sistema funcione.
        }

        // Trazabilidad 2: Empleado Asignado
        if (pedido.getEmpleadoAsignado() != null) {
            dto.setUsuIdEmpleado(pedido.getEmpleadoAsignado().getUsuId());
        }

        // Trazabilidad 3: Cliente
        if (pedido.getCliente() != null) {
            dto.setUsuIdCliente(pedido.getCliente().getUsuId());
        }


        if (pedido.getSesion() != null) {
            dto.setSesionId(pedido.getSesion().getSesId());
            String sesToken = pedido.getSesion().getSesToken();
            if (sesToken != null) {
                dto.setSesionToken(sesToken.substring(0, Math.min(sesToken.length(), 8)));
            }
        }


        dto.setConId(pedido.getConId());
        dto.setPedIdentificadorCliente(pedido.getPedIdentificadorCliente());

        return dto;
    }
}
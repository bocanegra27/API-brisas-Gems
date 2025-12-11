package com.example.libreria_api.dto.gestionpedidos;

import com.example.libreria_api.model.gestionpedidos.HistorialEstadoPedido;

public class HistorialMapper {

    public static HistorialResponseDTO toResponseDTO(HistorialEstadoPedido historial) {
        if (historial == null) return null;

        HistorialResponseDTO dto = new HistorialResponseDTO();
        dto.setHisId(historial.getHisId());
        dto.setHisFechaCambio(historial.getHisFechaCambio());
        dto.setHisComentarios(historial.getHisComentarios());
        dto.setHisImagen(historial.getHisImagen());

        // Mapear Estado
        if (historial.getEstadoPedido() != null) {
            dto.setEstId(historial.getEstadoPedido().getEst_id());
            dto.setEstadoNombre(historial.getEstadoPedido().getEstNombre());
        }

        // Mapear Responsable
        if (historial.getUsuarioResponsable() != null) {
            dto.setResponsableId(historial.getUsuarioResponsable().getUsuId());
            dto.setResponsableNombre(historial.getUsuarioResponsable().getUsuNombre());
        } else {
            dto.setResponsableNombre("Sistema/No asignado");
        }

        return dto;
    }
}
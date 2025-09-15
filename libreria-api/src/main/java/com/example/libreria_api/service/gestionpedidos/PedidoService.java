package com.example.libreria_api.service.gestionpedidos;

import com.example.libreria_api.model.gestionpedidos.Pedido;
import com.example.libreria_api.repository.gestionpedidos.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    public List<Pedido> obtenerTodosLosPedidos() {
        return pedidoRepository.findAll();
    }


    public Pedido guardarPedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public Pedido actualizar(Integer id, Pedido detalles) {
        return pedidoRepository.findById(id).map(pedidoExistente -> {
            pedidoExistente.setPedCodigo(detalles.getPedCodigo());
            pedidoExistente.setPedFechaCreacion(detalles.getPedFechaCreacion());
            pedidoExistente.setPedComentarios(detalles.getPedComentarios());
            pedidoExistente.setEstId(detalles.getEstId());
            pedidoExistente.setPerId(detalles.getPerId());
            pedidoExistente.setUsuId(detalles.getUsuId());
            return pedidoRepository.save(pedidoExistente);
        }).orElse(null);
    }

    public boolean eliminarPedido(Integer id) {
        if (pedidoRepository.existsById(id)) {
            pedidoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
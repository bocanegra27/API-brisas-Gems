package com.example.libreria_api.service.gestionpedidos;

import com.example.libreria_api.model.gestionpedidos.EstadoPedido;
import com.example.libreria_api.repository.gestionpedidos.EstadoPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EstadoPedidoService {

    @Autowired
    private EstadoPedidoRepository estadoPedidoRepository;

    public List<EstadoPedido> obtenerTodos() {
        return estadoPedidoRepository.findAll();
    }

    public EstadoPedido guardar(EstadoPedido estadoPedido) {
        return estadoPedidoRepository.save(estadoPedido);
    }

    public EstadoPedido actualizar(Integer id, EstadoPedido detalles) {
        return estadoPedidoRepository.findById(id).map(estadoExistente -> {
            estadoExistente.setEstNombre(detalles.getEstNombre());
            return estadoPedidoRepository.save(estadoExistente);
        }).orElse(null);
    }

    public void eliminar(Integer id) {
        estadoPedidoRepository.deleteById(id);
    }
}
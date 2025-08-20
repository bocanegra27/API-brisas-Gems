package com.example.libreria_api.service;

import com.example.libreria_api.model.Pedido;
import com.example.libreria_api.repository.PedidoRepository;
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
}
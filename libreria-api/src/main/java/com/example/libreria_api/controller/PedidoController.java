package com.example.libreria_api.controller;

import com.example.libreria_api.model.Pedido;
import com.example.libreria_api.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping("/pedidos")
    public List<Pedido> obtenerTodosLosPedidos() {
        return pedidoService.obtenerTodosLosPedidos();
    }

    @PostMapping("/pedidos")

    public Pedido crearPedido(@RequestBody Pedido pedido) {
        return pedidoService.guardarPedido(pedido);
    }

    @PutMapping("pedidos/{id}")
    public Pedido actualizar(@PathVariable Integer id, @RequestBody Pedido detalles) {
        return pedidoService.actualizar(id, detalles);
    }
}
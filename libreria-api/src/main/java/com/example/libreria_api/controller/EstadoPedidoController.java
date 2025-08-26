package com.example.libreria_api.controller;

import com.example.libreria_api.model.EstadoPedido;
import com.example.libreria_api.service.EstadoPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class EstadoPedidoController {

    @Autowired
    private EstadoPedidoService estadoPedidoService;

    @GetMapping("/estados-pedido")
    public List<EstadoPedido> obtenerTodos() {
        return estadoPedidoService.obtenerTodos();
    }

    @PostMapping("/estados-pedido")
    public EstadoPedido crear(@RequestBody EstadoPedido estadoPedido) {
        return estadoPedidoService.guardar(estadoPedido);
    }

    @PutMapping("estados-pedido/{id}")
    public EstadoPedido actualizar(@PathVariable Integer id, @RequestBody EstadoPedido detalles) {
        return estadoPedidoService.actualizar(id, detalles);
    }
}
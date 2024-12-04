package com.curso.ecommerce.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.curso.ecommerce.model.DetalleOrden;
import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.service.ProductoService;

@RestController
@RequestMapping("/api/cart")  // Prefijo para todos los endpoints del carrito
public class CarritoController {

    @Autowired
    private ProductoService productoService;

    // Lista para almacenar los detalles del carrito y la orden
    private List<DetalleOrden> detalles = new ArrayList<>();
    private Orden orden = new Orden();

    // Añadir un producto al carrito
    @PostMapping("/add")
    public ResponseEntity<Orden> addCart(@RequestParam Integer id, @RequestParam Integer cantidad) {
        Optional<Producto> optionalProducto = productoService.get(id);
        if (optionalProducto.isPresent()) {
            Producto producto = optionalProducto.get();
            DetalleOrden detalleOrden = new DetalleOrden();
            detalleOrden.setCantidad(cantidad);
            detalleOrden.setNombre(producto.getNombre());
            detalleOrden.setProducto(producto);

            // Añadir el detalle al carrito
            detalles.add(detalleOrden);

            // Calcular el total de la orden
            double sumaTotal = detalles.stream().mapToDouble(DetalleOrden::getTotal).sum();
            orden.setTotal(sumaTotal);

            return new ResponseEntity<>(orden, HttpStatus.CREATED);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Eliminar un producto del carrito
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Orden> removeCart(@PathVariable Integer id) {
        // Filtrar los detalles para eliminar el producto con el id dado
        detalles = detalles.stream()
                .filter(detalleOrden -> detalleOrden.getProducto().getId() != id)
                .collect(Collectors.toList());

        // Recalcular el total de la orden
        double sumaTotal = detalles.stream().mapToDouble(DetalleOrden::getTotal).sum();
        orden.setTotal(sumaTotal);

        return new ResponseEntity<>(orden, HttpStatus.OK);
    }

    // Ver el carrito
    @GetMapping("/view")
    public ResponseEntity<Orden> viewCart() {
        return new ResponseEntity<>(orden, HttpStatus.OK);
    }

    // Vaciar el carrito
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart() {
        detalles.clear();
        orden.setTotal(0.0);
        return ResponseEntity.noContent().build();
    }
}

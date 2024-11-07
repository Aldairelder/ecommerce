package com.curso.ecommerce.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.curso.ecommerce.model.DetalleOrden;
import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.service.ProductoService;

@Controller
@RequestMapping("")
public class HomeController {
    private final Logger log = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private ProductoService productoService;

    // Almacenar detalles en orden
    List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();
    
    // Datos de la orden
    Orden orden = new Orden();
    
    @GetMapping("")
    public String home(Model model) {
        model.addAttribute("productos", productoService.findAll());
        return "usuarios/home";
    }

    @GetMapping("productohome/{id}")
    public String productoHome(@PathVariable Integer id, Model model) {
        log.info("Id producto enviado como parametro {}", id);
        Optional<Producto> productoOptional = productoService.get(id);
        if (productoOptional.isPresent()) {
            Producto producto = productoOptional.get();
            model.addAttribute("producto", producto);
        } else {
            log.warn("Producto no encontrado con id: {}", id);
        }
        return "usuarios/productohome";
    }

    @PostMapping("/cart")
    public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model) {
        Optional<Producto> optionalProducto = productoService.get(id);
        if (optionalProducto.isPresent()) {
            Producto producto = optionalProducto.get();
            log.info("Producto añadido: {}", producto);
            log.info("Cantidad: {}", cantidad);
            
            DetalleOrden detalleOrden = new DetalleOrden();
            detalleOrden.setCantidad(cantidad);
            detalleOrden.setNombre(producto.getNombre());
            detalleOrden.setProducto(producto);
            
            // No necesitamos establecer el precio aquí, se calculará en getTotal()
            detalles.add(detalleOrden);
            
            // Calcular la suma total de los detalles
            double sumaTotal = detalles.stream().mapToDouble(DetalleOrden::getTotal).sum();
            orden.setTotal(sumaTotal);
            
            model.addAttribute("cart", detalles);
            model.addAttribute("orden", orden);
        } else {
            log.warn("No se encontró el producto con id: {}", id);
        }

        return "usuarios/carrito";
    }

    // Quitar un producto del carrito
    @GetMapping("/delete/cart/{id}")
    public String deleteProductoCart(@PathVariable Integer id, Model model) {
        List<DetalleOrden> ordenesNueva = new ArrayList<DetalleOrden>();

        for (DetalleOrden detalleOrden : detalles) {
            if (detalleOrden.getProducto().getId() != id) {
                ordenesNueva.add(detalleOrden);
            }
        }
        
        // Nueva lista con productos restantes
        detalles = ordenesNueva;

        // Calcular la suma total de los detalles después de eliminar
        double sumaTotal = detalles.stream().mapToDouble(DetalleOrden::getTotal).sum();
        orden.setTotal(sumaTotal);
        
        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);
        return "usuarios/carrito";
    }
}

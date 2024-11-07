package com.curso.ecommerce.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.model.categoria;
import com.curso.ecommerce.model.DetalleOrden;
import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.service.CategoriaService;
import com.curso.ecommerce.service.ProductoService;
import com.curso.ecommerce.service.UploadFileService;

@RestController
@RequestMapping("/api/productos")  // Prefijo para todos los endpoints en este controlador
public class ApiController {

    private final Logger LOGGER = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private ProductoService productoService;

    @Autowired
    private UploadFileService upload;

    @Autowired
    private CategoriaService categoriaService;

    // Listado de productos en el carrito
    private List<DetalleOrden> detalles = new ArrayList<>();
    private Orden orden = new Orden();

    // Obtener todos los productos
    @GetMapping
    public List<Producto> getAllProductos() {
        return productoService.findAll();
    }

    // Crear un nuevo producto
    @PostMapping("/save")
    public ResponseEntity<Producto> saveProducto(
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("cantidad") Integer cantidad,
            @RequestParam("precioCompra") Double precioCompra,
            @RequestParam("precioVenta") Double precioVenta,
            @RequestParam("categoria_id") Integer categoriaId,
            @RequestParam("descuento") Double descuento,
            @RequestParam("marca") String marca,
            @RequestParam("img") MultipartFile file) throws IOException {

        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setCantidad(cantidad);
        producto.setPrecioCompra(precioCompra); 
        producto.setPrecioVenta(precioVenta);

        // Obtener la categoría por ID
        categoria categoria = categoriaService.findById(categoriaId);
        producto.setCategoria(categoria);
        producto.setDescuento(descuento);
        producto.setMarca(marca);

        // Manejar la imagen
        if (file != null && !file.isEmpty()) {
            String nombreImagen = upload.saveImage(file);
            producto.setImagen(nombreImagen);
        }

        productoService.save(producto);
        return new ResponseEntity<>(producto, HttpStatus.CREATED);
    }

    // Obtener un producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Integer id) {
        Optional<Producto> optionalProducto = productoService.get(id);
        return optionalProducto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Actualizar un producto existente
    @PutMapping("/update")
    public ResponseEntity<Producto> updateProducto(
            @RequestParam Integer id,
            @RequestBody Producto producto,
            @RequestParam("img") MultipartFile file) throws IOException {

        Optional<Producto> optionalProducto = productoService.get(id);
        if (optionalProducto.isPresent()) {
            Producto p = optionalProducto.get();

            if (file.isEmpty()) {
                producto.setImagen(p.getImagen());
            } else {
                if (!p.getImagen().equals("default.jpg")) {
                    upload.deleteImage(p.getImagen());
                }
                String nombreImagen = upload.saveImage(file);
                producto.setImagen(nombreImagen);
            }

            productoService.update(producto);
            return new ResponseEntity<>(producto, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar un producto por ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Integer id) {
        Optional<Producto> optionalProducto = productoService.get(id);
        if (optionalProducto.isPresent()) {
            Producto p = optionalProducto.get();
            if (!p.getImagen().equals("default.jpg")) {
                upload.deleteImage(p.getImagen());
            }
            productoService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // --- Endpoints para el carrito ---

    // Añadir un producto al carrito
    @PostMapping("/cart/add")
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
    @DeleteMapping("/cart/remove/{id}")
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
    @GetMapping("/cart/view")
    public ResponseEntity<Orden> viewCart() {
        return new ResponseEntity<>(orden, HttpStatus.OK);
    }

    // Vaciar el carrito
    @DeleteMapping("/cart/clear")
    public ResponseEntity<Void> clearCart() {
        detalles.clear();
        orden.setTotal(0.0);
        return ResponseEntity.noContent().build();
    }
}

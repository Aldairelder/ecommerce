package com.curso.ecommerce.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.model.categoria;
import com.curso.ecommerce.service.CategoriaService;
import com.curso.ecommerce.service.ProductoService;
import com.curso.ecommerce.service.UploadFileService;

@RestController
@RequestMapping("/api/productos")  // Prefijo para todos los endpoints de productos
public class ApiController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private UploadFileService upload;

    @Autowired
    private CategoriaService categoriaService;

    // Obtener todos los productos
    @GetMapping
    public Iterable<Producto> getAllProductos() {
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
}

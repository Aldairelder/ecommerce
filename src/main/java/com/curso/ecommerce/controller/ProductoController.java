package com.curso.ecommerce.controller;

import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;

import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.model.categoria;
import com.curso.ecommerce.service.CategoriaService;
import com.curso.ecommerce.service.ProductoService;
import com.curso.ecommerce.service.UploadFileService;

@Controller
@RequestMapping("/productos")
public class ProductoController {
    private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);

    @Autowired
    private ProductoService productoService;

    @Autowired
    private UploadFileService upload;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("")
    public String show(Model model) {
        model.addAttribute("productos", productoService.findAll());
        return "productos/show";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("categorias", categoriaService.findAll());
        return "productos/create";
    }

    @PostMapping("/save")
    public String save(@RequestParam("nombre") String nombre,
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
        producto.setCategoria(categoria); // Establecer la categoría

        producto.setDescuento(descuento);
        producto.setMarca(marca);

        // Lógica para manejar la imagen
        if (file != null && !file.isEmpty()) {
            String nombreImagen = upload.saveImage(file);
            producto.setImagen(nombreImagen);
        }

        productoService.save(producto);
        return "redirect:/productos"; 
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Optional<Producto> optionalProducto = productoService.get(id);
        if (optionalProducto.isPresent()) {
            Producto producto = optionalProducto.get();
            LOGGER.info("Producto buscado: {}", producto);
            model.addAttribute("producto", producto);
            model.addAttribute("categorias", categoriaService.findAll());
            return "productos/edit";
        } else {
            return "redirect:/productos"; 
        }
    }

    @PostMapping("/update")
    public String update(Producto producto, @RequestParam("img") MultipartFile file, @RequestParam("categoria_id") Integer categoriaId) throws IOException {
        Optional<Producto> optionalProducto = productoService.get(producto.getId());
        if (optionalProducto.isPresent()) {
            Producto p = optionalProducto.get();
    
            // Establecer la categoría seleccionada por el usuario
            categoria categoria = categoriaService.findById(categoriaId);
            producto.setCategoria(categoria);
    
            // Manejar la imagen: si no hay nueva imagen, usar la existente
            if (file.isEmpty()) {
                producto.setImagen(p.getImagen());
            } else {
                if (!p.getImagen().equals("default.jpg")) {
                    upload.deleteImage(p.getImagen());
                }
                String nombreImagen = upload.saveImage(file);
                producto.setImagen(nombreImagen);
            }

            // Ya no se maneja el usuario

            // Actualizar el producto en la base de datos
            productoService.update(producto);
        } else {
            return "redirect:/productos"; 
        }
    
        return "redirect:/productos";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        Optional<Producto> optionalProducto = productoService.get(id);
        if (optionalProducto.isPresent()) {
            Producto p = optionalProducto.get();
            if (!p.getImagen().equals("default.jpg")) {
                upload.deleteImage(p.getImagen());
            }
            productoService.delete(id);
        }
        return "redirect:/productos";
    }
}

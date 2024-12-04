package com.curso.ecommerce.controller;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.model.Usuario;
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

    // Verificar si el usuario está logueado antes de mostrar productos
    @GetMapping("")
    public String show(Model model, HttpSession session) {
        if (session.getAttribute("idusuario") == null) {
            return "redirect:/auth/login"; // Redirigir a login si no hay sesión
        }
        model.addAttribute("productos", productoService.findAll());
        return "productos/show";
    }

    @GetMapping("/create")
    public String create(Model model, HttpSession session) {
        if (session.getAttribute("idusuario") == null) {
            return "redirect:/auth/login"; // Redirigir a login si no hay sesión
        }
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
                       @RequestParam("img") MultipartFile file,
                       HttpSession session) throws IOException {

        // Verificar que el usuario esté logueado
        if (session.getAttribute("idusuario") == null) {
            return "redirect:/auth/login"; // Redirigir si no está logueado
        }

        // Obtener el usuario logueado desde la sesión
        Integer idUsuario = (Integer) session.getAttribute("idusuario");
        Usuario usuario = new Usuario();
        usuario.setId(idUsuario); // Asignar el id del usuario logueado

        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setCantidad(cantidad);
        producto.setPrecioCompra(precioCompra); 
        producto.setPrecioVenta(precioVenta);

        // Asignar la categoría al producto
        categoria categoria = categoriaService.findById(categoriaId);
        producto.setCategoria(categoria);

        producto.setDescuento(descuento);
        producto.setMarca(marca);
        producto.setUsuario(usuario); // Asociar el usuario logueado al producto

        // Manejar la imagen
        if (file != null && !file.isEmpty()) {
            String nombreImagen = upload.saveImage(file);
            producto.setImagen(nombreImagen);
        }

        productoService.save(producto);
        return "redirect:/productos"; 
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model, HttpSession session) {
        if (session.getAttribute("idusuario") == null) {
            return "redirect:/auth/login"; // Redirigir a login si no hay sesión
        }

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
    public String update(Producto producto, @RequestParam("img") MultipartFile file, 
                         @RequestParam("categoria_id") Integer categoriaId, HttpSession session) throws IOException {

        if (session.getAttribute("idusuario") == null) {
            return "redirect:/auth/login"; // Redirigir si no está logueado
        }

        Optional<Producto> optionalProducto = productoService.get(producto.getId());
        if (optionalProducto.isPresent()) {
            Producto p = optionalProducto.get();

            // Asignar la categoría seleccionada
            categoria categoria = categoriaService.findById(categoriaId);
            producto.setCategoria(categoria);

            // Manejar la imagen
            if (file.isEmpty()) {
                producto.setImagen(p.getImagen());
            } else {
                if (!p.getImagen().equals("default.jpg")) {
                    upload.deleteImage(p.getImagen());
                }
                String nombreImagen = upload.saveImage(file);
                producto.setImagen(nombreImagen);
            }

            // Mantener el usuario existente
            producto.setUsuario(p.getUsuario());

            productoService.update(producto);
        }
        return "redirect:/productos";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, HttpSession session) {
        if (session.getAttribute("idusuario") == null) {
            return "redirect:/auth/login"; // Redirigir si no está logueado
        }

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

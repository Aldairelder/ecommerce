package com.curso.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.curso.ecommerce.service.IUsuarioService;

@Controller
@RequestMapping("/administrador")
public class AdministradorController {

    @Autowired
    private IUsuarioService usuarioService;

    // Vista principal del administrador
    @GetMapping("")
    public String home(Model model) {
        return "administrador/home";
    }

  
    @GetMapping("usuariosshow")
    public String show (Model model) {
        return "usuario/show";
    }
}

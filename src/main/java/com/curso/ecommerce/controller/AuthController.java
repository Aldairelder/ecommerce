package com.curso.ecommerce.controller;

import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IUsuarioService usuarioService;

    // Muestra el formulario de login
    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login"; // Vista del formulario de login
    }

    // Procesa el formulario de login
    @PostMapping("/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        HttpSession session, Model model) {
        Optional<Usuario> optionalUsuario = usuarioService.findByEmail(email);

        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();

            if (usuario.getPassword().equals(password)) {
                // Guardar usuario en sesión
                session.setAttribute("idusuario", usuario.getId());
                session.setAttribute("nombre", usuario.getNombre());

                return "redirect:/administrador"; // Redirigir a productos tras el login
            } else {
                model.addAttribute("error", "Contraseña incorrecta");
            }
        } else {
            model.addAttribute("error", "Usuario no encontrado");
        }

        return "auth/login"; // Volver al formulario en caso de error
    }

    // Cerrar sesión
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Eliminar sesión
        return "redirect:/auth/login"; // Redirigir al login
    }
}

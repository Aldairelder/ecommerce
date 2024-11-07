package com.curso.ecommerce.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class webconfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/productos/**") // Permitir CORS en este endpoint
                .allowedOrigins("http://localhost:5173") // Cambia esto al puerto de tu frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Métodos permitidos
                .allowCredentials(true); // Permite credenciales si es necesario
    }
}

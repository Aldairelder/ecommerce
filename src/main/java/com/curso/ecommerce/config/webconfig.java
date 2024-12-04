package com.curso.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class webconfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Permitir solicitudes de todos los orígenes (ajusta esto según tus necesidades)
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")  // Frontend en Vue.js (ajusta según la URL de tu frontend)
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true);  // Permite el uso de credenciales
    }
}

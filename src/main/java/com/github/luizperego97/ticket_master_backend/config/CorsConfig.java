package com.github.luizperego97.ticket_master_backend.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Libera todos os endpoints
                .allowedOrigins("http://localhost:5173", "http://localhost:3000") // Origens permitidas (ex: Vite ou React)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos permitidos
                .allowedHeaders("*") // Libera todos os cabeçalhos
                .allowCredentials(true); // Permite envio de cookies/autenticação se necessário
    }
}

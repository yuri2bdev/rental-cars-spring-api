package com.challenge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Permitir todas as origens para facilitar o desenvolvimento
        config.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://localhost:8080"));
        
        // Permitir todos os cabeçalhos
        config.addAllowedHeader("*");
        
        // Permitir todos os métodos (GET, POST, PUT, DELETE, etc)
        config.addAllowedMethod("*");
        
        // Permitir credenciais
        config.setAllowCredentials(true);
        
        // Expor cabeçalhos específicos
        config.setExposedHeaders(Arrays.asList("Content-Disposition", "Content-Type"));
        
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
} 
package com.empreget.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer{

	//Permissão de todas as origens, mas é necessário haver restrição por segurança
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		
		
		registry.addMapping("/**") //todas
        .allowedOrigins("http://localhost:8100", "http://localhost:8080", "http://localhost:3000", "http://localhost:4200") // 8100 ionic / máquina / angular web
        .allowedMethods("GET", "POST", "PUT", "DELETE") //("*")
        .allowedHeaders("*")
        .allowCredentials(true);
	}
}

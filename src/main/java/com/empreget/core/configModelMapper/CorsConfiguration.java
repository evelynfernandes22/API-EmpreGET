package com.empreget.core.configModelMapper;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer{

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		
		//Permissão de todas as origens, mas é necessário haver restrição por segurança
		
		registry.addMapping("/**")
        .allowedOrigins("http://localhost:8100") // Domínio que está rodando o ionic
        .allowedMethods("GET", "POST", "PUT", "DELETE")
        .allowedHeaders("*")
        .allowCredentials(true);
	}
}

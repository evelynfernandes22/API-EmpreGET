package com.empreget.core.config.openApi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SpringFoxConfig implements WebMvcConfigurer {

	 @Bean
	  public Docket apiDocket() {
	    return new Docket(DocumentationType.OAS_30)
	        .select()
	          .apis(RequestHandlerSelectors.basePackage("com.empreget.api"))
	          .paths(PathSelectors.any()) 
//	          .paths(PathSelectors.ant("/prestadores/*")) //filtrando por path
	          .build()
	          .apiInfo(apiInfo())
	          .tags(new Tag("Prestadores", "Gerencia prestadores"), 
	        		  new Tag("Clientes", "Gerencia clientes"),
	        		  new Tag("Ordens de Serviço", "Administra as Ordens de Serviço"),
	        		  new Tag("Foto", "Gerencia o upload e download de foto do prestador"),
	        		  new Tag("Usuario", "Gerencia usuários"),
	        		  new Tag("Avaliacao", "Gerencia as avaliações de prestadores"),
	        		  new Tag("Acesso","Login"));
	    				
	  }
	 
	 public ApiInfo apiInfo() {
		 return new ApiInfoBuilder()
				 .title("EmpreGET Api")
				 .description("Api aberta para comunidade. Objetiva a busca e contratação facilitada de prestadores de serviços domésticos na cidade de Londrina/Pr. "
				 		+ "A aplicação refere-se ao projeto de extensão do curso de Análise e Desenvolvimento de Sistemas da Unifil, "
				 		+ "desenvolvido pelos alunos: Evelyn Fernandes, Fernando Tunouti e Gedson Souza.")
				 .version("1")
				 .contact(new Contact("Unifil", "https://unifil.br/", "unifil@unifil.edu.br"))
				 .build();
	 }
	 
	 @Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		 registry.addResourceHandler("swagger-ui.html")
		 .addResourceLocations("classpath:/META-INF/resources/");
		 
		 registry.addResourceHandler("/webjars/**")
		 .addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
	  
}

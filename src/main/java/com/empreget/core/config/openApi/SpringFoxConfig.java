package com.empreget.core.config.openApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.empreget.api.dto.OrdemServicoResponse;
import com.empreget.api.dto.PrestadorResponse;
import com.empreget.api.dto.UsuarioResponse;
import com.empreget.api.exceptionHandler.Problem;
import com.empreget.api.openApi.model.OSResponseOpenApi;
import com.empreget.api.openApi.model.PageableModelOpenApi;
import com.empreget.api.openApi.model.PrestadorResponseOpenApi;
import com.empreget.api.openApi.model.UsuarioResponseOpenApi;
import com.fasterxml.classmate.TypeResolver;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RepresentationBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Response;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@Import(BeanValidatorPluginsConfiguration.class)
public class SpringFoxConfig implements WebMvcConfigurer {

	@Bean
	public Docket apiDocket() {
		TypeResolver typeResolver = new TypeResolver();
		
		return new Docket(DocumentationType.OAS_30)
				.select()
				.apis(RequestHandlerSelectors
						.basePackage("com.empreget.api"))
				.paths(PathSelectors.any())
				.build()				
				.useDefaultResponseMessages(false)
				.globalResponses(HttpMethod.GET, globalGetResponseMessages())
				.globalResponses(HttpMethod.POST, globalPostPutResponseMessages())
				.globalResponses(HttpMethod.PUT, globalPostPutResponseMessages())
				.globalResponses(HttpMethod.DELETE, globalDeleteResponseMessages())
				.additionalModels(typeResolver.resolve(Problem.class))
				.directModelSubstitute(Pageable.class,PageableModelOpenApi.class)
				.alternateTypeRules(AlternateTypeRules.newRule(typeResolver.resolve(Page.class, OrdemServicoResponse.class), OSResponseOpenApi.class))
				.alternateTypeRules(AlternateTypeRules.newRule(typeResolver.resolve(Page.class, PrestadorResponse.class), PrestadorResponseOpenApi.class))
				.alternateTypeRules(AlternateTypeRules.newRule(typeResolver.resolve(Page.class, UsuarioResponse.class), UsuarioResponseOpenApi.class))
				.apiInfo(apiInfo())
				.tags(new Tag("Prestadores", "Gerencia prestadores"), new Tag("Clientes", "Gerencia clientes"),
						new Tag("Ordens de Serviço", "Administra as Ordens de Serviço"),
						new Tag("Foto", "Gerencia o upload e download de foto do prestador"),
						new Tag("Usuario", "Gerencia usuários"),
						new Tag("Avaliacao", "Gerencia as avaliações de prestadores"), new Tag("Acesso", "Login"))
                .securitySchemes(Arrays.asList(apiKey()))
				.securityContexts(Arrays.asList(securityContext()));
				
	}
	
	
	private List<Response> globalGetResponseMessages() {
		return Arrays.asList(
				new ResponseBuilder()
					.code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
					.description("Erro interno do Servidor")
					.representation( MediaType.APPLICATION_JSON )
                    .apply(getProblemaModelReference())	
					.build(),
				new ResponseBuilder()
					.code(String.valueOf(HttpStatus.FORBIDDEN.value()))
					.description("Acesso negado")
					.representation( MediaType.APPLICATION_JSON )
                    .apply(getProblemaModelReference())	
					.build(),
				new ResponseBuilder()
					.code(String.valueOf(HttpStatus.UNAUTHORIZED.value()))
					.description("Acesso não autorizado")
					.representation( MediaType.APPLICATION_JSON )
                    .apply(getProblemaModelReference())	
					.build(),
				new ResponseBuilder()
					.code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))
					.description("Recurso não possui representação que pode ser aceita pelo consumidor")
					.build());
	}

	private List<Response> globalPostPutResponseMessages() {
		return Arrays.asList(
				new ResponseBuilder()
					.code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
					.description("Requisição inválida (erro do cliente)")
					.representation( MediaType.APPLICATION_JSON )
                    .apply(getProblemaModelReference())
					.build(),
				new ResponseBuilder()
					.code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
					.description("Erro interno no servidor")
					.representation( MediaType.APPLICATION_JSON )
                    .apply(getProblemaModelReference())
					.build(),
				new ResponseBuilder()
					.code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))
					.description("Recurso não possui representação que poderia ser aceita pelo consumidor")
					.build(),
				new ResponseBuilder()
					.code(String.valueOf(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()))
					.description("Requisição recusada porque o corpo está em um formato não suportado")
					.representation( MediaType.APPLICATION_JSON )
                    .apply(getProblemaModelReference())
					.build());
	}

	private List<Response> globalDeleteResponseMessages() {
		return Arrays.asList(
				new ResponseBuilder()
					.code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
					.description("Requisição inválida (erro do cliente)")
					.representation( MediaType.APPLICATION_JSON )
                    .apply(getProblemaModelReference())
					.build(),
				new ResponseBuilder()
					.code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
					.description("Erro interno no servidor")
					.representation( MediaType.APPLICATION_JSON )
                    .apply(getProblemaModelReference())
					.build());
	}

	public ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("EmpreGET Api")
				.description(
				"Api aberta para comunidade. Objetiva a busca e contratação facilitada de prestadores de serviços domésticos na cidade de Londrina/Pr. "
						+ "A aplicação refere-se ao projeto de extensão do curso de Análise e Desenvolvimento de Sistemas da Unifil, "
						+ "desenvolvido pelos alunos: Evelyn Fernandes, Fernando Tunouti e Gedson Souza.")
				.version("1")
				.contact(new Contact("Unifil", "https://unifil.br/", "unifil@unifil.edu.br"))
				.build();
	}


	private Consumer<RepresentationBuilder> getProblemaModelReference() {
	    return r -> r.model(m -> m.name("Problema")
	            .referenceModel(ref -> ref.key(k -> k.qualifiedModelName(
	                    q -> q.name("Problema").namespace("com.empreget.api.exceptionHandler")))));
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}	
	
	//Autenticação e autorização 
	public ApiKey apiKey() {
	        return new ApiKey("JWT", "Authorization", "header");
	}

	    private SecurityContext securityContext(){
	        return SecurityContext.builder()
	                .securityReferences(defaultAuth())
	                .forPaths(PathSelectors.any())
	                .build();
	    }

	    private List<SecurityReference> defaultAuth() {
	        AuthorizationScope authorizationScope = new AuthorizationScope(
	                "global", "accessEverything");
	        AuthorizationScope[] scopes = new AuthorizationScope[1];
	        scopes[0] = authorizationScope;
	        SecurityReference reference = new SecurityReference("JWT", scopes);
	        List<SecurityReference> auths = new ArrayList<>();
	        auths.add(reference);
	        return auths;
	    }
}

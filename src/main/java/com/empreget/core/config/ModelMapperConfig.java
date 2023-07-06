package com.empreget.core.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.empreget.api.dto.ClienteResponse;
import com.empreget.api.dto.PrestadorResponse;
import com.empreget.domain.model.Cliente;
import com.empreget.domain.model.Prestador;

@Configuration
public class ModelMapperConfig {
	
	@Bean
	public ModelMapper modelMapper() {

		var modelMapper = new ModelMapper();

		//método para fazer o ModelMapper aceitar o nome que eu quiser no atributo email, em vez de usuarioEmail a
		//para ser reconhecido.
		var usurioTousuarioModelTypeMap = modelMapper.createTypeMap(Cliente.class, ClienteResponse.class);
		usurioTousuarioModelTypeMap.<String>addMapping(usuarioSrc -> usuarioSrc.getUsuario().getEmail(),
				(usuarioModelDest, value) -> usuarioModelDest.setEmail(value));
		
		//o mesmo no prestador, configurando para encontrar o email na string informada no response
		var prestadorUserTouserModelTypeMap = modelMapper.createTypeMap(Prestador.class, PrestadorResponse.class);
		prestadorUserTouserModelTypeMap.<String>addMapping(usuarioSrc -> usuarioSrc.getUsuario().getEmail(),
				(usuarioModelDest, value) -> usuarioModelDest.setEmail(value));

		return modelMapper;

	}
	

}

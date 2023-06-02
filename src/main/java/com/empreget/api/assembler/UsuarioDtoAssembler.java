package com.empreget.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.empreget.api.dto.UsuarioResponse;
import com.empreget.domain.model.Usuario;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class UsuarioDtoAssembler {

	private ModelMapper modelMapper;
	
	public UsuarioResponse toModel(Usuario usuario) {
		return modelMapper.map(usuario, UsuarioResponse.class);
	}
	
	public List<UsuarioResponse> toCollectionModel(List<Usuario> usuarios){
		return usuarios.stream()
				.map(usuario -> toModel(usuario))
				.collect(Collectors.toList());
	}
}

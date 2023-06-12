package com.empreget.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.empreget.api.dto.input.UsuarioEmailInput;
import com.empreget.api.dto.input.UsuarioInput;
import com.empreget.domain.model.Usuario;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class UsuarioInputDisassembler {
	
	private ModelMapper modelMapper;
	
	public Usuario toDomainModel(UsuarioInput usuarioInput){
		return modelMapper.map(usuarioInput, Usuario.class);
	}
	
	public void copyToDomainObject(UsuarioInput usuarioInput, Usuario usuario) {
		modelMapper.map(usuarioInput, usuario);
	}
	
	public void copyToDomainObjectMail(UsuarioEmailInput usuarioEmailInput, Usuario usuario) {
		modelMapper.map(usuarioEmailInput, usuario);
	}
}

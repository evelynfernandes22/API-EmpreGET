package com.empreget.api.dto.input;

import javax.validation.constraints.NotBlank;

import com.empreget.domain.model.enums.UserRole;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioComSenhaInput  extends UsuarioInput{

	/*
	 * Para adicionar, puxando os dados do UsuarioInput, 
	 * de forma completa.
	 */
	@NotBlank
	private String senha;
	
	private UserRole role;
}

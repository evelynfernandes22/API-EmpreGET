package com.empreget.api.dto.input;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioSemSenhaInput {

	/*
	 * Para PUT atualizar
	 */
	
	@NotBlank
	private String nome;
	@Email
	@NotBlank
	private String email;
}

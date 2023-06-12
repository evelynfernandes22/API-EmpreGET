package com.empreget.api.dto.input;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioEmailInput{

	/*
	 * Para atualizar email, sem alterar o tipo 
	 * do usuário "cliente ou não. 
	 */
	
	@Email
	@NotBlank
	private String email;

	
}

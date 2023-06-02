package com.empreget.api.dto.input;

import javax.validation.constraints.NotBlank;

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
	
	private boolean souCliente;
}

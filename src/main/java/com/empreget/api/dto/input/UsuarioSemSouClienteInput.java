package com.empreget.api.dto.input;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioSemSouClienteInput  extends UsuarioInput{

	/*
	 * Para atualizar, sem alterar o tipo 
	 * do usuário "cliente ou não. 
	 */

	@NotBlank
	private String senha;
	
}

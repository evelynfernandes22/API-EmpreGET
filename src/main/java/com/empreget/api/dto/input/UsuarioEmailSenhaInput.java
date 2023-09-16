package com.empreget.api.dto.input;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioEmailSenhaInput  extends UsuarioInput{

	/*
	 * Para atualizar email e senha, sem alterar o tipo 
	 * do usuário "cliente ou não". 
	 */
	
	@ApiModelProperty(example = "123456", required = true)
	@NotBlank
	private String senha;
	
}

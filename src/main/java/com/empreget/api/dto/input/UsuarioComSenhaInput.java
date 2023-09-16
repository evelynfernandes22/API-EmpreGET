package com.empreget.api.dto.input;

import javax.validation.constraints.NotBlank;

import com.empreget.domain.model.enums.UserRole;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioComSenhaInput  extends UsuarioInput{

	/*
	 * Para adicionar, puxando os dados do UsuarioInput, 
	 * de forma completa.
	 */
	@ApiModelProperty(example = "123456", required = true)
	@NotBlank
	private String senha;
	
	private UserRole role;
}

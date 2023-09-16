package com.empreget.api.dto.input;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioInput {
	
	@ApiModelProperty(example = "nome@email.com", required = true)	
	@Email
	@NotBlank
	private String email;

}

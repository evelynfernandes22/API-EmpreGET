package com.empreget.api.dto.input;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AvaliacaoInput {

	@ApiModelProperty(required = true)
	@NotNull
	private Integer estrelas;
	
	private String comentario;
}

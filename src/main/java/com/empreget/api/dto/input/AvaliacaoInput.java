package com.empreget.api.dto.input;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AvaliacaoInput {

	@Valid
	@NotNull
	private PrestadorIdInput prestador;
	
	@NotNull
	private Integer estrelas;
	
	private String comentario;
}

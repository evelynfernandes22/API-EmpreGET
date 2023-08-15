package com.empreget.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AvaliacaoResponse {

	private Long id;
	private String nomePrestador;
	private String nomeCliente;
	private Integer estrelas;
}

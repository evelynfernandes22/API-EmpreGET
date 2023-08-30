package com.empreget.api.dto;

import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AvaliacaoResponse {

	private Long id;
	private String nomePrestador;
	private String nomeCliente;
	private Long idOrdemServico;
	private Integer estrelas;
	private String comentario;
	private OffsetDateTime dataDoCadastro;
}

package com.empreget.api.dto;

import com.empreget.domain.model.enums.Regiao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrestadorMinResponse {
	
	private long id;
	private String nome;
	private String enderecoCidade;
	private Regiao regiaoDisponivel; 
	private ServicoResponse servico;
	private String disponibilidade;
	private String detalhes;
}

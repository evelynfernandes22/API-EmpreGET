package com.empreget.api.dto;

import com.empreget.domain.model.enums.Regiao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrestadorMinResponse {
	
	private long id;
	private String imgUrl;
	private String nome;
	private String enderecoCidade;
	private Regiao regiao; 
	private ServicoResponse servico;
	private String disponibilidade;
	private String observacao;
}

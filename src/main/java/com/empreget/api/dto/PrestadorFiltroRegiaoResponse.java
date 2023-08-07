package com.empreget.api.dto;

import com.empreget.domain.model.enums.Regiao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrestadorFiltroRegiaoResponse {
	
	private Long id;
	private String imgUrl;
	private String nome;
	private Regiao regiao; 
	private String servicoValor;
}

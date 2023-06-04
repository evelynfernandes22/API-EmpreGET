package com.empreget.api.dto;

import lombok.Data;

@Data
public class ClienteMinResponse {
	
	private Long id;
	private String imgUrl;
	private String nome;
	private EnderecoResponse endereco;
	private String telefone;

}

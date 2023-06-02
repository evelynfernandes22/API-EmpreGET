package com.empreget.api.dto;

import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteResponse {
	
	private Long id;
	private String nome;
	private String imgUrl;
	private String rg;
	private String cpf;
	private EnderecoResponse endereco;
	private String telefone;
	private String email;
	private OffsetDateTime dataDoCadastro;
	private OffsetDateTime dataDaAtualizacao;
	

}

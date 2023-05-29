package com.empreget.api.dto;

import java.time.OffsetDateTime;

import lombok.Data;

@Data
public class ClienteResponse {
	
	private Long id;
	private String nome;
	private String rg;
	private String cpf;
	EnderecoResponse endereco;
	private String telefone;
	private String email;
	private OffsetDateTime dataDoCadastro;
	private OffsetDateTime dataDaAtualizacao;

}

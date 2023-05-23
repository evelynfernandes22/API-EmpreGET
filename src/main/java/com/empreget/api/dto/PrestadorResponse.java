package com.empreget.api.dto;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import com.empreget.domain.model.enums.Regiao;

import lombok.Data;

@Data
public class PrestadorResponse {
	
	private long id;
	private String nome;
	private EnderecoResponse endereco;
	private Regiao regiao; 
	private String rg;
	private String cpf;
	private String telefone;
	private String email;
	private ServicoResponse servico;
	private String disponibilidade;
	private String observacao;
	private OffsetDateTime dataDoCadastro;
	private OffsetDateTime dataDaAtualizacao;
}

package com.empreget.api.dto.input;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.empreget.api.dto.ServicoResponse;
import com.empreget.domain.model.enums.Regiao;

import lombok.Data;

@Data
public class PrestadorInput {

	@NotBlank
	private String nome;

	@Valid
	private EnderecoInput endereco;
	@NotNull
	private Regiao regiao; 
	@NotBlank
	private String rg;
	@NotBlank
	private String cpf;
	@NotBlank
	private String telefone;
	@Email
	@NotBlank
	private String email;
	@Valid
	private ServicoInput servico;
	@NotBlank
	private String disponibilidade;
	
	private String observacao;
}

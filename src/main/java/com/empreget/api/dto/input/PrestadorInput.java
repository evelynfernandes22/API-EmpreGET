package com.empreget.api.dto.input;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.empreget.api.dto.ServicoResponse;
import com.empreget.domain.model.enums.Regiao;

import lombok.Data;

@Data
public class PrestadorInput {

	private long id;
	
	@NotBlank
	private String nome;
	
	private EnderecoInput endereco;

	@NotNull
	private Regiao regiaoDisponivel; 
	
	@NotBlank
	private String rg;
	
	@NotBlank
	private String cpf;
	
	@NotBlank
	private String telefone;
	
	@Email
	@NotBlank
	private String email;
	
	private ServicoResponse servico;
	
	@NotBlank
	private String disponibilidade;
	
	@NotBlank
	private String detalhes;
}

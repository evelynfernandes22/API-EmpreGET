package com.empreget.api.dto.input;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteInput {


	@NotBlank
	private String nome;

	@Valid
	private EnderecoInput endereco;

	@NotBlank
	private String rg;

	@NotBlank(message = "CPF é obrigatório.")	
	//@CPF
	private String cpf;

	@NotBlank
	private String telefone;

	@Email
	@NotBlank
	private String email;

	
	
}

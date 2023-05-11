package com.empreget.api.dto.input;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.empreget.domain.model.Endereco;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteInput {

	@NotBlank
	private String nome;

	private Endereco endereco;

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

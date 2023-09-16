package com.empreget.api.dto.input;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.empreget.domain.model.enums.Regiao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PrestadorInput {

	@NotBlank
	private String nome;

	private String imgUrl;
	@Valid
	private UsuarioComSenhaInput usuario;
	@Valid
	private EnderecoInput endereco;
	@ApiModelProperty(required = true)
	@NotNull
	private Regiao regiao; 
	@NotBlank
	private String rg;
	@NotBlank
	private String cpf;
	@NotBlank
	private String telefone;
	@Valid
	private ServicoInput servico;
	@NotBlank
	private String disponibilidade;  
	
	private String observacao;
	
}

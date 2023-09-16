package com.empreget.api.dto.input;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnderecoInput {

	@NotBlank
	private String logradouro;
	
	@ApiModelProperty(example = "100", required = true)
	@NotNull
	private int numero;
	
	private String complemento;
	@NotBlank
	private String cep;
	@NotBlank
	private String bairro;
	@NotBlank
	private String cidade;
	@NotBlank
	private String estado;
	@NotBlank
	private String pais;
}

package com.empreget.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnderecoResponse {

	private String logradouro;
	private int numero;
	private String complemento;
	private String cep;
	private String bairro;
	private String cidade;
	private String estado;
	private String pais;
}

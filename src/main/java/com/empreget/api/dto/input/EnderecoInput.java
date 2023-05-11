package com.empreget.api.dto.input;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnderecoInput {

	private String logradouro;
	private int numero;
	private String complemento;
	private String cep;
	private String bairro;
	private String cidade;
	private String estado;
	private String pais;
}

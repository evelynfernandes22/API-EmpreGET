package com.empreget.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CidadeResponse {

	private String logradouro;
	private int numero;
	private String complemento;
	private String bairro;
	private String cidade;
	private String estado;
	private String pais;
}

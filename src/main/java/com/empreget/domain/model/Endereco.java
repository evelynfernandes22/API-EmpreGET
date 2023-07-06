package com.empreget.domain.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;


@Data
@Embeddable
public class Endereco {
	
	@Column(name = "end_logradouro")
	private String logradouro;
	
	@Column(name = "end_numero")
	private int numero;
	
	@Column(name = "end_complemento")
	private String complemento;
	
	@Column(name = "end_cep")
	private String cep;
	
	@Column(name = "end_bairro")
	private String bairro;
	
	@Column(name = "end_cidade")
	private String cidade;
	
	@Column(name = "end_estado")
	private String estado;
	
	@Column(name = "end_pais")
	private String pais;


}

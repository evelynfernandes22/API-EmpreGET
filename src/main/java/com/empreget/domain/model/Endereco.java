package com.empreget.domain.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

import lombok.Data;


@Data
@Embeddable
public class Endereco {
	
	@NotBlank
	@Column(name = "end_logradouro")
	private String logradouro;
	
	@NotBlank
	@Column(name = "end_numero")
	private int numero;
	
	@Column(name = "end_complemento")
	private String complemento;
	
	@NotBlank
	@Column(name = "end_cep")
	private String cep;
	
	@NotBlank
	@Column(name = "end_bairro")
	private String bairro;
	
	@NotBlank
	@Column(name = "end_cidade")
	private String cidade;
	
	@NotBlank
	@Column(name = "end_estado")
	private String estado;
	
	@NotBlank
	@Column(name = "end_pais")
	private String pais;


}

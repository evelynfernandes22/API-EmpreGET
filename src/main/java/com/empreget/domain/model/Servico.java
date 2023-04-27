package com.empreget.domain.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Servico {
	
	@NotBlank
	@Column (name = "servico_descricao")
	private String descricao;
	
	@NotNull
	@Column (name = "servico_valor")
	private BigDecimal valor;
	
	

}

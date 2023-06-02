package com.empreget.api.dto.input;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServicoInput {

	@NotBlank
	private String descricao;
	@NotNull
	private BigDecimal valor;
}

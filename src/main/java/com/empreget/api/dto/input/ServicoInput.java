package com.empreget.api.dto.input;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServicoInput {

	@NotBlank
	private String descricao;
	@ApiModelProperty(required = true)
	@NotNull
	private BigDecimal valor;
}

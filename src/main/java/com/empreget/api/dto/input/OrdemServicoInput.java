package com.empreget.api.dto.input;

import java.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.empreget.domain.model.enums.Periodo;
import com.empreget.domain.model.enums.TipoDiaria;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdemServicoInput {

	@ApiModelProperty(example = "1", required = true)
	@Valid
	@NotNull
	private PrestadorIdInput prestador;
	
	@ApiModelProperty(example = "2023-09-20", required = true)
	@NotNull
	private LocalDate dataServico;
	
	@ApiModelProperty(example = "COMERCIAL", required = true)
	@NotNull
	private Periodo periodo;
	
	@ApiModelProperty(example = "DIARIA_CHEIA", required = true)
	@NotNull
	private TipoDiaria tipoDeDiaria;
}

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

	@ApiModelProperty(example = "1")
	@Valid
	@NotNull
	private PrestadorIdInput prestador;
	
	@NotNull
	private LocalDate dataServico;
	
	@NotNull
	private Periodo periodo;
	
	@NotNull
	private TipoDiaria tipoDeDiaria;
}

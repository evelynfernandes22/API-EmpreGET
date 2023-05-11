package com.empreget.api.dto.input;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.empreget.domain.model.enums.TipoDiaria;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoInput {

	@Valid
	@NotNull
	private ClienteIdInput cliente;
	
	@Valid
	@NotNull
	private PrestadorIdInput prestador;
	
//IMPLEMENTAR
//	private List<Agenda> agenda;
	
	@NotNull
	private TipoDiaria tipoDeDiaria;
}

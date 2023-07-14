package com.empreget.api.dto;

import java.time.LocalDate;

import com.empreget.domain.model.enums.StatusAgenda;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdemServicoDataResponse {
	
	private Long id;
	private String nomeCliente;
	private String nomePrestador;
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("dataServico")
	private LocalDate dataServico;
	private StatusAgenda statusAgenda;

}

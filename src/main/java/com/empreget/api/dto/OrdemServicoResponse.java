package com.empreget.api.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import com.empreget.domain.model.enums.Periodo;
import com.empreget.domain.model.enums.StatusAgenda;
import com.empreget.domain.model.enums.StatusOrdemServico;
import com.empreget.domain.model.enums.TipoDiaria;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdemServicoResponse {
	
	private Long id;
	private String nomeCliente;
	private EnderecoResponse LocalDoServico;
	private String nomePrestador;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("dataServico")
	private LocalDate dataServico;
	private Periodo periodo;
	private StatusAgenda statusAgenda;
	
	private TipoDiaria tipoDeDiaria;
	private StatusOrdemServico statusOrdemServico;
	private ServicoResponse servico;
	private OffsetDateTime dataDaSolicitacao;
	private OffsetDateTime dataDaFinalizacao;

}

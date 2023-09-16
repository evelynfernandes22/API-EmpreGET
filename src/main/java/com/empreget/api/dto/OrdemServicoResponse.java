package com.empreget.api.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import com.empreget.domain.model.enums.Periodo;
import com.empreget.domain.model.enums.StatusAgenda;
import com.empreget.domain.model.enums.StatusOrdemServico;
import com.empreget.domain.model.enums.TipoDiaria;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdemServicoResponse {
	
	@ApiModelProperty(example = "1")
	private Long id;
	
	@ApiModelProperty(example = "Ana Pereira")
	private String nomeCliente;
	
	private EnderecoResponse LocalDoServico;
	
	@ApiModelProperty(example = "Sueli Cavalcanti")
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

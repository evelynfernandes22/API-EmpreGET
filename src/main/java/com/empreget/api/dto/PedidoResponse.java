package com.empreget.api.dto;

import java.time.OffsetDateTime;

import com.empreget.domain.model.enums.StatusPedido;
import com.empreget.domain.model.enums.TipoDiaria;

import lombok.Data;

@Data
public class PedidoResponse {
	
	private Long id;
	private String nomeCliente;
	private EnderecoResponse LocalDoServico;
	private String nomePrestador;
	private TipoDiaria tipoDeDiaria;
	private StatusPedido status;
	private ServicoResponse servico;
	private OffsetDateTime dataDaSolicitacao;
	private OffsetDateTime dataDaFinalizacao;
	

}

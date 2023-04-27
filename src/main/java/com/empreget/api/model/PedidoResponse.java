package com.empreget.api.model;

import java.time.OffsetDateTime;

import com.empreget.domain.model.StatusPedido;
import com.empreget.domain.model.TipoDiaria;

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
	private OffsetDateTime dataDoPedido;
	private OffsetDateTime dataDaFinalizacao;
	

}

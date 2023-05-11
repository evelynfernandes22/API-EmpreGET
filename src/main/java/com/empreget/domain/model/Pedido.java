package com.empreget.domain.model;

import java.time.OffsetDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;

import com.empreget.domain.ValidationGroups;
import com.empreget.domain.exception.NegocioException;
import com.empreget.domain.model.enums.StatusPedido;
import com.empreget.domain.model.enums.TipoDiaria;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@Entity
public class Pedido {
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Valid
	@ConvertGroup(from = Default.class, to = ValidationGroups.ClienteId.class)	
	@NotNull
	@ManyToOne
	@JoinColumn(name="cliente_id", nullable=false)
	private Cliente cliente;
	
	@Valid
	@ConvertGroup(from = Default.class, to = ValidationGroups.PrestadorId.class)
	@NotNull
	@ManyToOne
	@JoinColumn(name="prestador_id", nullable=false)
	private Prestador prestador;
	
//IMPLEMENTAR
//	private List<Agenda> agenda;
	
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private TipoDiaria tipoDeDiaria;
	
	@JsonProperty(access = Access.READ_ONLY)
	@Enumerated(EnumType.STRING)
	private StatusPedido status;
	
	@JsonProperty(access = Access.READ_ONLY)
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private OffsetDateTime dataDaSolicitacao;
	
	@JsonProperty(access = Access.READ_ONLY)
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private OffsetDateTime dataDaFinalizacao;
	

	public void aceitar() {
		if(!podeSerAceito()) {
			throw new NegocioException("O pedido não pode ser aceito.");
		}
		setStatus(StatusPedido.PENDENTE);
	}
	
	public void recusar() {
		if(!getStatus().equals(StatusPedido.AGUARDANDO_ACEITE)) {
			throw new NegocioException("Este pedido não pode ser recusado, por não estar no status [aguardando aceite].");
		}
		setStatus(StatusPedido.RECUSADO);
		setDataDaFinalizacao(OffsetDateTime.now());
	}
	
	public void finalizar() {
		if(!podeSerFinalizado()) {
			throw new NegocioException("O pedido não pode ser finalizado");
		}
		
		setStatus(StatusPedido.FINALIZADO);
		setDataDaFinalizacao(OffsetDateTime.now());
		
	}
	
	
	public boolean podeSerAceito() {
		return StatusPedido.AGUARDANDO_ACEITE.equals(getStatus());
	}
	
	public boolean podeSerFinalizado() {
		return StatusPedido.PENDENTE.equals(getStatus());
	}
	
	
	
	
	

}

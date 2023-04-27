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
import javax.validation.constraints.NotNull;

import com.empreget.domain.exception.NegocioException;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Pedido {
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//@Valid
	//@ConvertGroup(from = Default.class, to = ValidationGroups.ClienteId.class)	
	@ManyToOne
	@JoinColumn(name="cliente_id", nullable=false)
	private Cliente cliente;
	
	//@Valid
	//@ConvertGroup(from = Default.class, to = ValidationGroups.PrestadorId.class)
	@ManyToOne
	@JoinColumn(name="prestador_id", nullable=false)
	private Prestador prestador;
	

//	private List<Agenda> agenda;
	
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private TipoDiaria tipoDeDiaria;
	
	@JsonProperty(access = Access.READ_ONLY)
	@Enumerated(EnumType.STRING)
	private StatusPedido status;
	
	@JsonProperty(access = Access.READ_ONLY)
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private OffsetDateTime dataDoPedido;
	
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

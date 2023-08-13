package com.empreget.domain.model;

import java.time.LocalDate;
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
import com.empreget.domain.model.enums.Periodo;
import com.empreget.domain.model.enums.StatusAgenda;
import com.empreget.domain.model.enums.StatusOrdemServico;
import com.empreget.domain.model.enums.TipoDiaria;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@Entity
public class OrdemServico {
	
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
	
	@NotNull
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate dataServico;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private Periodo periodo;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private StatusAgenda statusAgenda;

	@NotNull
	@Enumerated(EnumType.STRING)
	private TipoDiaria tipoDeDiaria;
	
	@JsonProperty(access = Access.READ_ONLY)
	@Enumerated(EnumType.STRING)
	private StatusOrdemServico statusOrdemServico;
	
	@JsonProperty(access = Access.READ_ONLY)
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private OffsetDateTime dataDaSolicitacao;
	
	@JsonProperty(access = Access.READ_ONLY)
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private OffsetDateTime dataDaFinalizacao;
		
		
/*
 * MÉTODOS
 */
	public void aceitar() {
		if(!podeSerAceito()) {
			throw new NegocioException("O pedido não pode ser aceito.");
		}
		setStatusOrdemServico(StatusOrdemServico.PENDENTE);
		setStatusAgenda(StatusAgenda.RESERVADO);
	}
	
	public void recusar() {
		if(!getStatusOrdemServico().equals(StatusOrdemServico.AGUARDANDO_ACEITE)) {
			throw new NegocioException("Este pedido não pode ser recusado, por não estar no status [aguardando aceite].");
		}
		setStatusOrdemServico(StatusOrdemServico.RECUSADO);
		setStatusAgenda(StatusAgenda.DISPONÍVEL);
		setDataDaFinalizacao(OffsetDateTime.now());
	}
	
	public void finalizar() {
		if(!podeSerFinalizado()) {
			throw new NegocioException("O pedido não pode ser finalizado");
		}
		
		setStatusOrdemServico(StatusOrdemServico.FINALIZADO);
		setStatusAgenda(StatusAgenda.INDISPONIVEL);
		setDataDaFinalizacao(OffsetDateTime.now());
	}
	
	public void cancelar() {
		if(!podeSerCancelado()) {
			throw new NegocioException("A ordem de serviço não pode ser cancelada, pois está em fase de execução pelo prestador");
		}else if(getStatusOrdemServico() == StatusOrdemServico.CANCELADO) {
			throw new NegocioException("Não foi possível executar esta operação, pois a ordem de serviço "
					+ "já está cancelada");
		}
		
		setStatusOrdemServico(StatusOrdemServico.CANCELADO);
		setStatusAgenda(StatusAgenda.DISPONÍVEL);
		setDataDaFinalizacao(OffsetDateTime.now());
	}
	
	public boolean podeSerAceito() {
		return StatusOrdemServico.AGUARDANDO_ACEITE.equals(getStatusOrdemServico());
	}
	
	public boolean podeSerFinalizado() {
		return StatusOrdemServico.PENDENTE.equals(getStatusOrdemServico());
	}
	public boolean podeSerCancelado() {
		return podeSerAceito() || podeSerFinalizado();
	}

}

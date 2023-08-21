package com.empreget.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class FotoPrestador {

	@EqualsAndHashCode.Include
	@Id
	@Column(name = "prestador_id")
	private Long id;
	
	@OneToOne(fetch = FetchType.LAZY)
	@MapsId 
	private Prestador prestador;
	
	private String nomeArquivo;
	private String contentType;
	private Long tamanho;
}

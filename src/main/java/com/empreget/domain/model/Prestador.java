package com.empreget.domain.model;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.empreget.domain.ValidationGroups;
import com.empreget.domain.model.enums.Regiao;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Prestador {

	@NotNull(groups = ValidationGroups.PrestadorId.class)
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	private Usuario usuario;

	@NotBlank
	@Size(max = 60)
	private String nome;

	@Size(max = 255)
	private String imgUrl;

	@Embedded
	private Endereco endereco;

	@NotBlank
	@Size(max = 10)
	private String rg;

	@NotBlank(message = "CPF é obrigatório.")
	// @CPF
	private String cpf;

	@NotBlank
	@Size(max = 20)
	private String telefone;

	@Embedded
	private Servico servico;
	
	@NotBlank
	@Size(max = 255)
	@Column(name= "disponibilidade_na_semana")
	private String disponibilidade;

	@Size(max = 255)
	private String observacao;

	@NotNull
	@Enumerated(EnumType.STRING)
	private Regiao regiao;

	@JsonIgnore
	@CreationTimestamp
	@Column(nullable = false, columnDefinition = "datetime")
	private OffsetDateTime dataDoCadastro;

	@JsonIgnore
	@UpdateTimestamp
	@Column(nullable = false, columnDefinition = "datetime")
	private OffsetDateTime dataDaAtualizacao;

	@OneToMany(mappedBy = "prestador", cascade = CascadeType.ALL)
	private List<OrdemServico> ordensServico = new ArrayList<>();
	

}

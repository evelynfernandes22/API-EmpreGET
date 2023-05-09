package com.empreget.domain.model;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.empreget.domain.ValidationGroups;
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
	private long id;

	@NotBlank
	@Size(max = 60)
	private String nome;

	@Embedded
	private Endereco endereco;

	@NotBlank
	@Size(max = 10)
	private String rg;

	@NotBlank(message = "CPF é obrigatório.")
	//@CPF
	private String cpf;

	@NotBlank
	@Size(max = 20)
	private String telefone;

	@Email
	@NotBlank
	@Size(max = 255)
	private String email;

	@Embedded
	private Servico servico;
	
	@NotBlank
	@Size(max = 255)
	@Column(name= "disponibilidade_na_semana")
	private String disponibilidade;
	
	@NotBlank
	@Size(max = 255)
	@Column(name= "detalhes_sobre_mim")
	private String detalhes;
	
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private RegiaoDisponivel regiaoDisponivel;
	
	@JsonIgnore
	@CreationTimestamp
	@Column(nullable = false, columnDefinition = "datetime")
	private OffsetDateTime dataDoCadastro;
	
	@JsonIgnore
	@UpdateTimestamp
	@Column(nullable = false, columnDefinition = "datetime")
	private LocalDateTime dataDaAtualizacao;
	
}

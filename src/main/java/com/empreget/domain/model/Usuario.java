package com.empreget.domain.model;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Usuario {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Size(max = 60)
	private String nome;
	
	@Email
	@Column(name = "login")
    @NotEmpty(message = "Campo obrigatório.Não pode ser vazio.")
    private String email;

	@NotEmpty(message = "Campo obrigatório.")
    private String senha;

    private boolean souCliente;
    
	@Column(nullable = false, columnDefinition = "datetime")
	@CreationTimestamp
	private OffsetDateTime dataDoCadastro;
	
	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "usuario_grupo", 
		joinColumns = @JoinColumn(name="usuario_id"),
		inverseJoinColumns = @JoinColumn (name="grupo_id"))
	private List<Grupo> grupos = new ArrayList<>();
	
	
	//MÉTODOS
	public boolean senhaCoincideCom(String senha) {
		return getSenha().equals(senha);
	}
	
	public boolean senhaNaoCoincidemCom(String senha) {
		return !senhaCoincideCom(senha);
	}
	
}

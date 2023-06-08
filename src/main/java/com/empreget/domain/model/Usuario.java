package com.empreget.domain.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;

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
	
	
	//MÉTODOS
	public boolean senhaCoincideCom(String senha) {
		return getSenha().equals(senha);
	}
	
	public boolean senhaNaoCoincidemCom(String senha) {
		return !senhaCoincideCom(senha);
	}
	
}

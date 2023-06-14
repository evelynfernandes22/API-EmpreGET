package com.empreget.domain.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

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
	
	@NotBlank
	private String nome;
	
	@Email
    @NotEmpty(message = "Campo obrigatório.Não pode ser vazio.")
    private String email;

	@NotEmpty(message = "Campo obrigatório.")
    private String senha;

    private boolean souCliente;
    
	
	//MÉTODOS
	public boolean senhaCoincideCom(String senha) {
		return getSenha().equals(senha);
	}
	
	public boolean senhaNaoCoincidemCom(String senha) {
		return !senhaCoincideCom(senha);
	}
	
	public boolean credenciaisCorretas(String email, String senha) {
		return getEmail().equals(email) && getSenha().equals(senha);
	}
	public boolean credenciaisIncorretas(String email, String senha) {
		return !credenciaisCorretas(email, senha);
	}
	
}

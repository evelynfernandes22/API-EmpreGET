package com.empreget.domain.model;

import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.empreget.domain.model.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario implements UserDetails {
	
	
	private static final long serialVersionUID = 1L;

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

	@Enumerated(EnumType.STRING)
	private UserRole role;

	
	public Usuario(String nome, String email, String senha, UserRole role){
        this.nome = nome;
		this.email = email;
        this.senha = senha;
        this.role = role;
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		if(this.role == UserRole.ADMIN){
			return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_PRESTADOR"),new SimpleGrantedAuthority("ROLE_CLIENTE"));
		}else if(this.role == UserRole.CLIENTE){
			return List.of(new SimpleGrantedAuthority("ROLE_CLIENTE"));
		}else return List.of(new SimpleGrantedAuthority("ROLE_PRESTADOR"));
	}

	@Override
	public String getPassword() {
		return this.senha;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	
	//MÉTODOS ANTERIORES
	public boolean senhaCoincideCom(String senha) {
		return getSenha().equals(senha);
	}
	
	public boolean senhaNaoCoincidemCom(String senha) {
		return !senhaCoincideCom(senha);
	}

}

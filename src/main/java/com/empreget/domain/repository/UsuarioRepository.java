package com.empreget.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.empreget.domain.model.Usuario;

public interface UsuarioRepository extends JpaRepository <Usuario, Long>  {
	 
	Optional<UserDetails> findByEmail(String email); 
	Optional<Usuario> findUserByEmail(String email); 
	
}

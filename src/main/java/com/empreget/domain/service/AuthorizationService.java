package com.empreget.domain.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.empreget.domain.repository.UsuarioRepository;

@Transactional
@Service
public class AuthorizationService implements UserDetailsService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<UserDetails> optional = usuarioRepository.findByEmail(username);
		return optional.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
		
	}

}

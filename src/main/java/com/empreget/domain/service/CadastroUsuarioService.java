package com.empreget.domain.service;

import javax.transaction.Transactional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.empreget.domain.exception.EntidadeEmUsoException;
import com.empreget.domain.exception.NegocioException;
import com.empreget.domain.exception.PrestadorNaoEncontradoException;
import com.empreget.domain.exception.UsuarioNaoEncontradoException;
import com.empreget.domain.model.Usuario;
import com.empreget.domain.repository.UsuarioRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CadastroUsuarioService {

	private UsuarioRepository usuarioRepository;


	@Transactional
	public Usuario cadastrarUser(Usuario usuario) {
		
		boolean emailEmUso = usuarioRepository.findByEmail(usuario.getEmail()).stream()
				.anyMatch(usuarioExistente -> !usuarioExistente.equals(usuario));

		if (emailEmUso) {
			throw new NegocioException(String.format("Já existe um usuário cadastrado com o e-mail %s.", usuario.getEmail()));
		}
		
		String encryptedPassword = new BCryptPasswordEncoder().encode(usuario.getPassword()); //pegando o hash da senha do usuário
		Usuario novoUsuario = new Usuario(usuario.getNome(), usuario.getEmail(), encryptedPassword, usuario.getRole());
		return usuarioRepository.save(novoUsuario);
	}

	@Transactional
	public Usuario salvarEdicao(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}
	
	
	@Transactional
	public void alterarSenha(Long usuarioId, String senhaInformada, String novaSenha) {
		Usuario usuario = buscarOuFalhar(usuarioId);
				
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		if(!passwordEncoder.matches(senhaInformada, usuario.getSenha())) {
			throw new NegocioException("Senha atual informada não coincide com a senha do usuário");
		}
		
		String novaSenhaCriptografada = new BCryptPasswordEncoder().encode(novaSenha);
		usuario.setSenha(novaSenhaCriptografada);
	}
	
		
	public Usuario buscarOuFalhar(Long usuarioId) {
		return usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));
	}

	@Transactional
	public void excluir (Long usuarioId) {
		try {
			usuarioRepository.deleteById(usuarioId);
			usuarioRepository.flush();
		
		}catch (EmptyResultDataAccessException e) {
			throw new UsuarioNaoEncontradoException(usuarioId);
			
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format("Usuário de código %d não pode ser removido, "
							+ "pois está em uso", usuarioId));
		}
	}
	
}

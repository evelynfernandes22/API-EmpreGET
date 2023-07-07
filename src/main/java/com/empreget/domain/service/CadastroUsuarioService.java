package com.empreget.domain.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.empreget.domain.exception.NegocioException;
import com.empreget.domain.exception.UsuarioNaoEncontradoException;
import com.empreget.domain.model.Usuario;
import com.empreget.domain.repository.UsuarioRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CadastroUsuarioService {

	private UsuarioRepository usuarioRepository;

	@Transactional
	public Usuario salvar(Usuario usuario) {
		boolean emailEmUso = usuarioRepository.findByEmail(usuario.getEmail()).stream()
				.anyMatch(usuarioExistente -> !usuarioExistente.equals(usuario));

		if (emailEmUso) {
			throw new NegocioException(String.format("Já existe um usuário cadastrado com o e-mail %s.", usuario.getEmail()));
		}
				
		return usuarioRepository.save(usuario);
	}

	@Transactional
	public void alterarSenha(Long usuarioId, String senhaAtual, String novaSenha) {
		Usuario usuario = buscarOuFalhar(usuarioId);
		if(usuario.senhaNaoCoincidemCom(senhaAtual)) {
			throw new NegocioException("Senha atual informada não coincide com a senha do usuário");
		}
		usuario.setSenha(novaSenha);
	}
	
		
	public Usuario buscarOuFalhar(Long usuarioId) {
		return usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));
	}
	
	
//MÉTODOS DE AUTENTICAÇÃO POR SESSÃO

	public Optional<Usuario> autenticarUsuario(String email, String senha) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            usuario.isSouCliente();
            if (usuario.getSenha().equals(senha)) {
                return usuarioOptional;
            }
        }

        return Optional.empty();
    }
	
	
	
	
//	NÃO DEU CERTO	
	
//	@Transactional
//	public Usuario verificarCredenciais(String email, String senha) {
//		List<Usuario> usuarios = usuarioRepository.findAll();
//		
//		Usuario credenciaisValidas = null;
//		for (Usuario usuario : usuarios) {
//			if (usuario.credenciaisIncorretas(email, senha)) {
//				throw new NegocioException("E-mail ou senha inválidos. Tente novamente.");
//			}else if(usuario.credenciaisCorretas(email, senha)) {
//				return credenciaisValidas = usuario;
//			}
//		}
//		
//		return credenciaisValidas;
//	}
//	
//	@Transactional
//	 public void entrarNoAmbiente(Usuario usuario) {
//		
//	        if (usuario.isSouCliente()) {
//	            System.out.println(String.format("Entrando no ambiente de cliente com o e-mail: %s ", usuario.getEmail()));
//	        } else {
//	        	System.out.println(String.format("Entrando no ambiente de prestador com o e-mail: %s ", usuario.getEmail()));
//	        }
//	    }
	
}

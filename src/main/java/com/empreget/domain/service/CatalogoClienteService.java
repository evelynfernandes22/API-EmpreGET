package com.empreget.domain.service;

import javax.transaction.Transactional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.empreget.domain.exception.ClienteNaoEncontradoException;
import com.empreget.domain.exception.EntidadeEmUsoException;
import com.empreget.domain.exception.NegocioException;
import com.empreget.domain.model.Cliente;
import com.empreget.domain.repository.ClienteRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CatalogoClienteService {
	
	private static final String MSG_CLIENTE_EM_USO = "Cliente de código %d não pode ser removido, pois está em uso";
	private final ClienteRepository clienteRepository;

	
	public Cliente findById(Long clienteId) {
		return clienteRepository.findById(clienteId)
				.orElseThrow(() -> new NegocioException("Cliente não Encontrado."));
	}
	

	@Transactional
	public Cliente salvar(Cliente cliente) {
		boolean emailEmUso = clienteRepository.findByUsuarioEmail(cliente.getUsuario().getEmail()).stream()
				.anyMatch(clienteExistente -> !clienteExistente.equals(cliente));


		if (emailEmUso) {
			throw new NegocioException(String.format("Já existe um cliente cadastrado com o e-mail %d.",
					cliente.getUsuario().getEmail()));
		}

		return clienteRepository.save(cliente);
	}

	@Transactional
	public void excluir(Long clienteId) {
		try {
			clienteRepository.deleteById(clienteId);
			clienteRepository.flush(); //forçar commit no BD para descarregar a pilha
			
		}catch (EmptyResultDataAccessException e) {
			throw new ClienteNaoEncontradoException(clienteId);
			
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format(MSG_CLIENTE_EM_USO, clienteId));
		}
	}

	public Cliente buscarOuFalhar (Long clienteId) {
		return clienteRepository.findById(clienteId)
				.orElseThrow(() -> new ClienteNaoEncontradoException(clienteId));
	}

}

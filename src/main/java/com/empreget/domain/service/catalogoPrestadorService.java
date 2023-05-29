package com.empreget.domain.service;

import javax.transaction.Transactional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.empreget.domain.exception.EntidadeEmUsoException;
import com.empreget.domain.exception.NegocioException;
import com.empreget.domain.exception.PrestadorNaoEncontradoException;
import com.empreget.domain.model.Prestador;
import com.empreget.domain.repository.PrestadorRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CatalogoPrestadorService {
	
	private static final String MSG_PRESTADOR_EM_USO = "Prestador de código %d não pode ser removido, pois está em uso";
	
	private PrestadorRepository prestadorRepository;
	
	@Transactional
	public Prestador salvar(Prestador prestador) {
		boolean emailEmUso = prestadorRepository.findByEmail(prestador.getEmail()).stream()
				.anyMatch(prestadorExistente -> !prestadorExistente.equals(prestador));
		
		if (emailEmUso) {
			throw new NegocioException("Já existe um prestador cadastrado com este e-mail.");
		}
		return prestadorRepository.save(prestador);
	}
	
	@Transactional
	public void excluir (Long prestadorId) {
		try {
			prestadorRepository.deleteById(prestadorId);
			prestadorRepository.flush();
		
		}catch (EmptyResultDataAccessException e) {
			throw new PrestadorNaoEncontradoException(prestadorId);
			
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format(MSG_PRESTADOR_EM_USO, prestadorId));
		}
	}
	
	public Prestador buscarOuFalhar(Long prestadorId) {
		return prestadorRepository.findById(prestadorId)
				.orElseThrow(() -> new PrestadorNaoEncontradoException(prestadorId));
	}

}

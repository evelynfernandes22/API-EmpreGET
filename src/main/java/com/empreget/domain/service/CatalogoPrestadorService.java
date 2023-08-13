package com.empreget.domain.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.empreget.domain.exception.EntidadeEmUsoException;
import com.empreget.domain.exception.NegocioException;
import com.empreget.domain.exception.PrestadorNaoEncontradoException;
import com.empreget.domain.model.OrdemServico;
import com.empreget.domain.model.Prestador;
import com.empreget.domain.model.enums.Regiao;
import com.empreget.domain.repository.PrestadorRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CatalogoPrestadorService {
	
	private static final String MSG_PRESTADOR_EM_USO = "Prestador de código %d não pode ser removido, pois está em uso";
	
	private PrestadorRepository prestadorRepository;
	
	@Transactional
	public Prestador salvar(Prestador prestador) {
		boolean emailEmUso = prestadorRepository.findByUsuarioEmail(prestador.getUsuario().getEmail())
				.stream()
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
	
	@Transactional
	public Page<Prestador> obterPrestadoresPorRegiao(Regiao regiao, Pageable pageable) {
        return prestadorRepository.findAllByRegiao(regiao, pageable);
    }
	
	@Transactional
	public Page<Prestador> buscarPorNomeContem(String nome, Pageable pageable){
		return prestadorRepository.findByNomeContaining(nome, pageable);
	}
	
	@Transactional
	public List<OrdemServico>buscarOrdensServicoPorDataServico(Long prestadorId, LocalDate dataServico){
		Prestador prestador = buscarOuFalhar(prestadorId);
		
		return prestador.getOrdensServico().stream()
				.filter(ordemServico -> ordemServico.getDataServico().equals(dataServico))
				.collect(Collectors.toList());
	}

	public Prestador buscarOuFalhar(Long prestadorId) {
		return prestadorRepository.findById(prestadorId)
				.orElseThrow(() -> new PrestadorNaoEncontradoException(prestadorId));
	}
	
}

package com.empreget.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.empreget.domain.exception.AvaliacaoNaoEncontradoException;
import com.empreget.domain.exception.EntidadeNaoEncontradaException;
import com.empreget.domain.exception.NegocioException;
import com.empreget.domain.model.Avaliacao;
import com.empreget.domain.model.Cliente;
import com.empreget.domain.model.Prestador;
import com.empreget.domain.repository.AvaliacaoRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AvaliacaoService {
	
	private AvaliacaoRepository avaliacaoRepository;
	private CatalogoClienteService catalogoClienteService;
	private CatalogoPrestadorService catalogoPrestadorService;
	
	public Avaliacao avaliar(Avaliacao avaliacao) {
		
		Cliente cliente = catalogoClienteService.buscarOuFalhar(avaliacao.getCliente().getId());
		Prestador prestador = catalogoPrestadorService.buscarOuFalhar(avaliacao.getPrestador().getId());
		
		if(avaliacao.getEstrelas() > 5 || avaliacao.getEstrelas() < 0) {
			throw new NegocioException(String.format("A quantidade '%d' está fora da escala. Tente avaliar "
					+ "utilizando números inteiros de 1 a 5.", avaliacao.getEstrelas()));
		}
		
		avaliacao.setPrestador(prestador);
		avaliacao.setCliente(cliente);
		
		return avaliacaoRepository.save(avaliacao);
	}

	public double calcularMediaAvaliacoes(Long prestadorId) {
		
		Prestador prestador = catalogoPrestadorService.buscarOuFalhar(prestadorId);
		List<Avaliacao> avaliacoes = avaliacaoRepository.findByPrestadorId(prestadorId);
		if (avaliacoes.isEmpty()) {
			throw new AvaliacaoNaoEncontradoException(String.format("Até o momento, não há avaliações registradas para o prestador: %s.", prestador.getNome()));
		}

		double somaAvaliacoes = 0.0;
		for (Avaliacao avaliacao : avaliacoes) {
			somaAvaliacoes += avaliacao.getEstrelas();
		}

		return somaAvaliacoes / avaliacoes.size();
	}
}

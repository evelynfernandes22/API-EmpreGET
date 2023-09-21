package com.empreget.domain.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.empreget.domain.exception.AvaliacaoNaoEncontradoException;
import com.empreget.domain.exception.NegocioException;
import com.empreget.domain.exception.OrdemServicoNaoEncontradoException;
import com.empreget.domain.model.Avaliacao;
import com.empreget.domain.model.Cliente;
import com.empreget.domain.model.OrdemServico;
import com.empreget.domain.model.Prestador;
import com.empreget.domain.repository.AvaliacaoRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AvaliacaoService {
	
	private AvaliacaoRepository avaliacaoRepository;
	private CatalogoClienteService catalogoClienteService;
	private CatalogoPrestadorService catalogoPrestadorService;
	private BuscaOSService buscaOS;
	
	public Avaliacao avaliar(Avaliacao avaliacao) {
		
		Long prestadorId = avaliacao.getOrdemServico().getPrestador().getId();
		Long clienteId = avaliacao.getOrdemServico().getCliente().getId();
		Long ordemServicoId = avaliacao.getOrdemServico().getId();
		
		Cliente cliente = catalogoClienteService.buscarOuFalhar(clienteId);
		Prestador prestador = catalogoPrestadorService.buscarOuFalhar(prestadorId);
		OrdemServico os = buscaOS.buscarOuFalhar(ordemServicoId);
		
		if(avaliacao.getEstrelas() > 5 || avaliacao.getEstrelas() < 0) {
			throw new NegocioException(String.format("A quantidade '%d' está fora da escala. Tente avaliar "
					+ "utilizando números inteiros de 1 a 5.", avaliacao.getEstrelas()));
		}
				
		avaliacao.setPrestador(prestador);
		avaliacao.setCliente(cliente);
		avaliacao.setOrdemServico(os);
		avaliacao.setDataDoCadastro(OffsetDateTime.now());
		
		return avaliacaoRepository.save(avaliacao);
	}


	public double calcularMediaAvaliacoes(Long prestadorId) {
		Prestador prestador = catalogoPrestadorService.buscarOuFalhar(prestadorId);
		List<Avaliacao> avaliacoes = avaliacaoRepository.findByPrestadorId(prestadorId);
				
		if (avaliacoes.isEmpty()) {
			throw new AvaliacaoNaoEncontradoException(String.format("Até o momento, não há avaliações registradas para o prestador: %s.", prestador.getNome()));
		}

		double somaAvaliacoes = avaliacoes.stream()
				.mapToDouble(Avaliacao::getEstrelas)
				.sum();
				
		return somaAvaliacoes / avaliacoes.size();
	}

	public Long calcularQuantidadeAvaliacoes(Long prestadorId) {
		catalogoPrestadorService.buscarOuFalhar(prestadorId);
		long quantidadeAvaliacoes = avaliacaoRepository.countByPrestadorId(prestadorId);
		
		return quantidadeAvaliacoes;
	}
	
	
}

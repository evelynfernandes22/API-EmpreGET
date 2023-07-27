package com.empreget.domain.service;

import org.springframework.stereotype.Service;

import com.empreget.domain.exception.OrdemServicoNaoEncontradoException;
import com.empreget.domain.model.OrdemServico;
import com.empreget.domain.repository.OrdemServicoRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class BuscaOSService {
	
	private OrdemServicoRepository ordemServicoRepositoy;

	public OrdemServico buscarOuFalhar (Long pedidoId) {
		return ordemServicoRepositoy.findById(pedidoId)
				.orElseThrow(() -> new OrdemServicoNaoEncontradoException(pedidoId));
	}
}

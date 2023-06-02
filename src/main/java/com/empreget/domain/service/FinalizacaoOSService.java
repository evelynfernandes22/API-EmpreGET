package com.empreget.domain.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.empreget.domain.model.OrdemServico;
import com.empreget.domain.repository.OrdemServicoRepositoy;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class FinalizacaoOSService {

	private OrdemServicoRepositoy ordemServicoRepositoy;
	private BuscaOSService buscaOSService;
	
	@Transactional
	public void finalizar (Long ordemServicoId) {
		OrdemServico ordemServico = buscaOSService.buscarOuFalhar(ordemServicoId);
		
		ordemServico.finalizar();
		
		ordemServicoRepositoy.save(ordemServico);
	}
}

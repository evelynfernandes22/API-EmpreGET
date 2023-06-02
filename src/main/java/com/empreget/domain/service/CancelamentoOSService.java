package com.empreget.domain.service;

import javax.transaction.Transactional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.empreget.domain.exception.OrdemServicoNaoEncontradoException;
import com.empreget.domain.model.OrdemServico;
import com.empreget.domain.repository.OrdemServicoRepositoy;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CancelamentoOSService {
	
	private OrdemServicoRepositoy ordemServicoRepositoy;
	private BuscaOSService buscaOSService;
	
	@Transactional
	public void cancelar (Long ordemServicoId) {
		
		try {
			OrdemServico ordemServico = buscaOSService.buscarOuFalhar(ordemServicoId);
			
			ordemServico.cancelar();
			
			ordemServicoRepositoy.save(ordemServico);
			
		
		}catch (EmptyResultDataAccessException e) {
			throw new OrdemServicoNaoEncontradoException(ordemServicoId);
			
		}
	}
	
	
	
}

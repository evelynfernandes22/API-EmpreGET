package com.empreget.domain.service;

import javax.transaction.Transactional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.empreget.domain.exception.NegocioException;
import com.empreget.domain.exception.OrdemServicoNaoEncontradoException;
import com.empreget.domain.model.OrdemServico;
import com.empreget.domain.model.enums.StatusOrdemServico;
import com.empreget.domain.repository.OrdemServicoRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CancelamentoOSService {

	private OrdemServicoRepository ordemServicoRepositoy;
	private BuscaOSService buscaOSService;
	
	@Transactional
	public void cancelar (Long ordemServicoId) {
		
		try {
			OrdemServico ordemServico = buscaOSService.buscarOuFalhar(ordemServicoId);
			
			if(ordemServico.getStatusOrdemServico().equals(StatusOrdemServico.AGUARDANDO_ACEITE)) {
				ordemServico.cancelar();
				ordemServicoRepositoy.save(ordemServico);
			} else {
				throw new NegocioException(String.format("Não é possível cancelar a ordem de serviço número %d, pois já foi aceita pelo prestador %s ",
						ordemServico.getId(), ordemServico.getPrestador().getNome()));
			}
		
		}catch (EmptyResultDataAccessException e) {
			throw new OrdemServicoNaoEncontradoException(ordemServicoId);
			
		}
	}
	
	
	
}

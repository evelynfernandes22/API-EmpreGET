package com.empreget.domain.service;

import java.time.OffsetDateTime;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.empreget.domain.model.Cliente;
import com.empreget.domain.model.OrdemServico;
import com.empreget.domain.model.Prestador;
import com.empreget.domain.model.enums.StatusAgenda;
import com.empreget.domain.model.enums.StatusOrdemServico;
import com.empreget.domain.repository.OrdemServicoRepositoy;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class SolicitacaoOSService {

	private OrdemServicoRepositoy ordemServicoRepositoy;
	private CatalogoClienteService catalogoClienteService;
	private CatalogoPrestadorService catalogoPrestadorService;
	private BuscaOSService buscaOSService;
	
	
	@Transactional
	public OrdemServico solicitar (OrdemServico ordemServico) {

		Cliente cliente = catalogoClienteService.buscarOuFalhar(ordemServico.getCliente().getId());
		Prestador prestador = catalogoPrestadorService.buscarOuFalhar(ordemServico.getPrestador().getId());
				
		ordemServico.setCliente(cliente);
		ordemServico.setPrestador(prestador);
		ordemServico.setStatusOrdemServico(StatusOrdemServico.AGUARDANDO_ACEITE);
		ordemServico.setStatusAgenda(StatusAgenda.PRE_RESERVADO);
		ordemServico.setDataDaSolicitacao(OffsetDateTime.now());
		
		return ordemServicoRepositoy.save(ordemServico);
	}
	
		
	@Transactional
	public void aceitar(Long pedidoId) {
		OrdemServico ordemServico = buscaOSService.buscarOuFalhar(pedidoId);
		
		ordemServico.aceitar();
		
		ordemServicoRepositoy.save(ordemServico);
	}
	
	@Transactional
	public void recusar (Long pedidoId) {
		OrdemServico ordemServico = buscaOSService.buscarOuFalhar(pedidoId);
		
		ordemServico.recusar();
		
		ordemServicoRepositoy.save(ordemServico);
	}

	
}

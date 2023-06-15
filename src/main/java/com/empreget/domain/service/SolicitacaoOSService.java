package com.empreget.domain.service;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.empreget.domain.exception.NegocioException;
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
	
	 private Map<OffsetDateTime, ReentrantLock> locks = new ConcurrentHashMap<>();
	
	
	@Transactional
	public OrdemServico solicitar(OrdemServico ordemServico) {
		
		OffsetDateTime dataServico = ordemServico.getDataServico();
		ReentrantLock lock = locks.computeIfAbsent(dataServico, k -> new ReentrantLock());

		lock.lock();

		try {
			
			Cliente cliente = catalogoClienteService.buscarOuFalhar(ordemServico.getCliente().getId());
			Prestador prestador = catalogoPrestadorService.buscarOuFalhar(ordemServico.getPrestador().getId());

			if (ordemServicoRepositoy.existsByPrestadorAndDataServico(prestador, dataServico)) {
				throw new NegocioException("Já existe uma ordem de serviço para o mesmo prestador na mesma data");
			}
			ordemServico.setCliente(cliente);
			ordemServico.setPrestador(prestador);
			ordemServico.setStatusOrdemServico(StatusOrdemServico.AGUARDANDO_ACEITE);
			ordemServico.setStatusAgenda(StatusAgenda.PRE_RESERVADO);
			ordemServico.setDataDaSolicitacao(OffsetDateTime.now());

			return ordemServicoRepositoy.save(ordemServico);
		 } finally {
	            lock.unlock();
	     }
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
	
	@Transactional
	public void finalizar (Long ordemServicoId) {
		OrdemServico ordemServico = buscaOSService.buscarOuFalhar(ordemServicoId);
		
		ordemServico.finalizar();
		
		ordemServicoRepositoy.save(ordemServico);
	}

	
}

package com.empreget.domain.service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.empreget.domain.exception.NegocioException;
import com.empreget.domain.model.Cliente;
import com.empreget.domain.model.OrdemServico;
import com.empreget.domain.model.Prestador;
import com.empreget.domain.model.enums.StatusAgenda;
import com.empreget.domain.model.enums.StatusOrdemServico;
import com.empreget.domain.repository.OrdemServicoRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class SolicitacaoOSService {

	private OrdemServicoRepository ordemServicoRepositoy;
	private CatalogoClienteService catalogoClienteService;
	private CatalogoPrestadorService catalogoPrestadorService;
	private BuscaOSService buscaOSService;
	
	 private Map<LocalDate, ReentrantLock> locks = new ConcurrentHashMap<>();
	
	
	@Transactional
	public OrdemServico solicitar(OrdemServico ordemServico) {
		
		LocalDate dataServico = ordemServico.getDataServico();
		
		ReentrantLock lock = locks.computeIfAbsent(dataServico, k -> new ReentrantLock());

		lock.lock(); //uma trava é adiquirida de ReentrantLock

		try {
			
			Cliente cliente = catalogoClienteService.buscarOuFalhar(ordemServico.getCliente().getId());
			Prestador prestador = catalogoPrestadorService.buscarOuFalhar(ordemServico.getPrestador().getId());

			boolean clienteJaSolicitouNaData = ordemServicoRepositoy.clienteJaSolicitouOSNaData(cliente, dataServico, Arrays.asList(StatusOrdemServico.FINALIZADO, StatusOrdemServico.CANCELADO));

			if (ordemServicoRepositoy.existsByPrestadorAndDataServico(prestador, dataServico)) {
				throw new NegocioException("Já existe uma ordem de serviço para o mesmo prestador na mesma data");
			}
			
			if(clienteJaSolicitouNaData) {
				throw new NegocioException(String.format("O cliente %s já possui uma ordem de serviço ativa na data %s ", ordemServico.getCliente().getNome(), dataServico));
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

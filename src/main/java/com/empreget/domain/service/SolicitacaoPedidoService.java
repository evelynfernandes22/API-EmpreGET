package com.empreget.domain.service;

import java.time.OffsetDateTime;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.empreget.domain.model.Cliente;
import com.empreget.domain.model.Pedido;
import com.empreget.domain.model.Prestador;
import com.empreget.domain.model.enums.StatusPedido;
import com.empreget.domain.repository.PedidoRepositoy;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class SolicitacaoPedidoService {

	private PedidoRepositoy pedidoRepositoy;
	private CatalogoClienteService catalogoClienteService;
	private catalogoPrestadorService catalogoPrestadorService;
	private BuscaPedidoService buscaPedidoService;
	
	
	@Transactional
	public Pedido solicitar (Pedido pedido) {

		Cliente cliente = catalogoClienteService.buscarOuFalhar(pedido.getCliente().getId());
		
		Prestador prestador = catalogoPrestadorService.buscarOuFalhar(pedido.getPrestador().getId());
		
		pedido.setCliente(cliente);
		pedido.setPrestador(prestador);
		pedido.setStatus(StatusPedido.AGUARDANDO_ACEITE);
		pedido.setDataDaSolicitacao(OffsetDateTime.now());
		
		return pedidoRepositoy.save(pedido);
	}
	
		
	@Transactional
	public void aceitar(Long pedidoId) {
		Pedido pedido = buscaPedidoService.buscarOuFalhar(pedidoId);
		
		pedido.aceitar();
		
		pedidoRepositoy.save(pedido);
	}
	
	@Transactional
	public void recusar (Long pedidoId) {
		Pedido pedido = buscaPedidoService.buscarOuFalhar(pedidoId);
		
		pedido.recusar();
		
		pedidoRepositoy.save(pedido);
	}

	
}

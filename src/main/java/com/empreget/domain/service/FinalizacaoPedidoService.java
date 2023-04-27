package com.empreget.domain.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.empreget.domain.model.Pedido;
import com.empreget.domain.repository.PedidoRepositoy;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class FinalizacaoPedidoService {

	private PedidoRepositoy pedidoRepositoy;
	private BuscaPedidoService buscaPedidoService;
	
	@Transactional
	public void finalizar (Long pedidoId) {
		Pedido pedido = buscaPedidoService.buscarOuFalhar(pedidoId);
		
		pedido.finalizar();
		
		pedidoRepositoy.save(pedido);
	}
}

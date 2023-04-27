package com.empreget.domain.service;

import org.springframework.stereotype.Service;

import com.empreget.domain.exception.PedidoNaoEncontradoException;
import com.empreget.domain.model.Pedido;
import com.empreget.domain.repository.PedidoRepositoy;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class BuscaPedidoService {
	
	private PedidoRepositoy pedidoRepositoy;

	public Pedido buscarOuFalhar (Long pedidoId) {
		return pedidoRepositoy.findById(pedidoId)
				.orElseThrow(() -> new PedidoNaoEncontradoException(pedidoId));
	}
}

package com.empreget.domain.exception;

public class OrdemServicoNaoEncontradoException extends EntidadeNaoEncontradaException {
	
	private static final long serialVersionUID = 1L;
	
	public OrdemServicoNaoEncontradoException(String mensagem){
		super(mensagem);
	}

	public OrdemServicoNaoEncontradoException (Long pedidoId) {
		this(String.format("Não existe um pedido com o código %d",pedidoId));
	}
}

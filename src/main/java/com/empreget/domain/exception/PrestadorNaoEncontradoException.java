package com.empreget.domain.exception;


public class PrestadorNaoEncontradoException extends EntidadeNaoEncontradaException {
	
	private static final long serialVersionUID = 1L;
	
	public PrestadorNaoEncontradoException(String mensagem){
		super(mensagem);
	}

	public PrestadorNaoEncontradoException (Long prestadorId) {
		this(String.format("Não existe um cadastro de prestador com o código %d",prestadorId));
	}
}

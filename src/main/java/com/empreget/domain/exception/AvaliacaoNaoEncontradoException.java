package com.empreget.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AvaliacaoNaoEncontradoException extends EntidadeNaoEncontradaException {
	
	private static final long serialVersionUID = 1L;
	
	public AvaliacaoNaoEncontradoException(String mensagem){
		super(mensagem);
	}

	public AvaliacaoNaoEncontradoException (Long prestadorId) {
		this(String.format("Não existem avaliações até o momento para o prestador de código %d",prestadorId));
	}
}

package com.empreget.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class FotoNaoEncontradaException extends EntidadeNaoEncontradaException {
	
	private static final long serialVersionUID = 1L;
	
	public FotoNaoEncontradaException(String mensagem){
		super(mensagem);
	}

	public FotoNaoEncontradaException (Long prestadorId) {
		this(String.format("Não existe uma foto cadastrada para o prestador de código %d",prestadorId));
	}
}

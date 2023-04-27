package com.empreget.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ClienteNaoEncontradoException extends EntidadeNaoEncontradaException {
	
	private static final long serialVersionUID = 1L;
	
	public ClienteNaoEncontradoException(String mensagem){
		super(mensagem);
	}

	public ClienteNaoEncontradoException (Long clienteId) {
		this(String.format("Não existe um cadastro de cliente com o código %d",clienteId));
	}
}

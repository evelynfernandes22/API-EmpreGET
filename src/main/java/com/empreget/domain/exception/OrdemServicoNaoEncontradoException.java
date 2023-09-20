package com.empreget.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class OrdemServicoNaoEncontradoException extends EntidadeNaoEncontradaException {
	
	private static final long serialVersionUID = 1L;
	
	public OrdemServicoNaoEncontradoException(String mensagem){
		super(mensagem);
	}

	public OrdemServicoNaoEncontradoException (Long ordemServicoId) {
		this(String.format("Não existe uma ordem de serviço com o código %d",ordemServicoId));
	}
}

package com.empreget.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class AcessoNegadoException extends AccessDeniedException {
	
	private static final long serialVersionUID = 1L;

	public AcessoNegadoException(String message){
        super(message);
    }

}

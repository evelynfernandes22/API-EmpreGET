package com.empreget.api.openApi.controller;

import org.springframework.http.ResponseEntity;

import com.empreget.api.dto.input.UsuarioEmailSenhaInput;
import com.empreget.api.exceptionHandler.Problem;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "Acesso")
public interface AutenticacaoControllerOpenApi {

	@ApiOperation("Efetua login na aplicação")
	@ApiResponses({
		@ApiResponse(code=200, message="Logado e autenticado", response= Problem.class)
	})
	public ResponseEntity login(UsuarioEmailSenhaInput input);
}

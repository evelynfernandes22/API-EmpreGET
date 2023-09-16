package com.empreget.api.openApi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.empreget.api.dto.UsuarioResponse;
import com.empreget.api.dto.input.SenhaInput;
import com.empreget.api.dto.input.UsuarioEmailInput;
import com.empreget.api.exceptionHandler.Problem;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "Usuario")
public interface UsuarioControllerOpenApi {

	@ApiOperation("Lista usuários")
	public Page<UsuarioResponse> listar(Pageable pageable);

	@ApiOperation("Busca usuário por Id")
	public UsuarioResponse buscar(@ApiParam(value = "Id de um usuário") Long usuarioId);

	@ApiOperation("Busca usuário por email")
	public UsuarioResponse buscarUsuarioPorEmail(@ApiParam(value = "E-mail de um usuário") String email);

	@ApiOperation("Atualiza email do usuário")
	@ApiResponses({
		@ApiResponse(code=200, message = "E-mail atualizado", response = Problem.class)
	})
	public UsuarioResponse atualizarEmail(@ApiParam(value = "Id de um usuário") Long usuarioId, UsuarioEmailInput usuarioInput);

	@ApiOperation("Altera a senha do usuário")
	@ApiResponses({
		@ApiResponse(code=204, message="Senha alterada", response= Problem.class),
	})
	public void alterarSenha(@ApiParam(value = "Id de um usuário") Long usuarioId, SenhaInput senhaInput);
}

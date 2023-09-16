package com.empreget.api.openApi.controller;

import java.util.List;

import com.empreget.api.dto.ClienteResponse;
import com.empreget.api.dto.input.ClienteInput;
import com.empreget.api.exceptionHandler.Problem;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "Clientes")
public interface ClienteControllerOpenApi {
	
	@ApiOperation("Lista clientes")
	public List<ClienteResponse> listar();
	
	@ApiOperation("Busca cliente por Id")
	public ClienteResponse buscarPorId(@ApiParam(value = "Id de um cliente") Long clienteId);

	@ApiOperation("Efetua cadastro de cliente")
	@ApiResponses({
		@ApiResponse(code=201, message="Cliente cadastrado", response= Problem.class)
	})
	public ClienteResponse adicionar(ClienteInput clienteInput);
	
	@ApiOperation("Edita cadastro de cliente")
	@ApiResponses({
		@ApiResponse(code=200, message="cadastro editado", response= Problem.class)
	})
	public ClienteResponse editar(@ApiParam(value = "Id de um cliente") Long clienteId, ClienteInput clienteinput);

	@ApiOperation("Exclui cadastro de cliente")
	@ApiResponses({
		@ApiResponse(code=204, message="Cadastro de cliente excluído do sistema", response= Problem.class)
	})
	public void excluir(@ApiParam(value = "Id de um cliente") Long clienteId);

}

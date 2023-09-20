package com.empreget.api.openApi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import com.empreget.api.dto.OrdemServicoResponse;
import com.empreget.api.dto.input.OrdemServicoInput;
import com.empreget.api.exceptionHandler.Problem;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "Ordens de Serviço")
public interface OSControllerOpenApi {

	@ApiOperation("Solicita/Agenda serviço com o prestador")
	@ApiResponses({
		@ApiResponse(code=201, message="Diária solicitada com sucesso.")
	})
	public OrdemServicoResponse solicitar(@ApiParam(name="corpo", value="Representação de uma nova Ordem de Serviço") OrdemServicoInput ordemServicoInput);		

	@ApiOperation("Lista Ordens de Serviço")
	public Page<OrdemServicoResponse> listar(Pageable pageable);

	@ApiOperation("Busca Ordem de Serviço por Id")
	@ApiResponses({
		@ApiResponse(code=400, message="Ordem de serviço inválida", response= Problem.class),
		@ApiResponse(code=404, message="Ordem de serviço não encontrada", response= Problem.class)
	})
	public OrdemServicoResponse buscar(@ApiParam(value = "Id de uma Ordem de Serviço") Long id);
	
	@ApiOperation("Cancela Ordem de Serviço")
	@ApiResponses({
		@ApiResponse(code=204, message="Serviço cancelado", response= Problem.class),
	})
	public void cancelar (@ApiParam(value = "Id de uma Ordem de Serviço") Long ordemServicoId);	
	
	@ApiOperation("Aceita solicitação de Ordem de Serviço")
	@ApiResponses({
		@ApiResponse(code=204, message="Serviço aceito pelo prestador", response= Problem.class),
	})
	public void aceitar (@ApiParam(value = "Id de uma Ordem de Serviço") Long id);
	
	@ApiOperation("Recusa solicitação de Ordem de Serviço")
	@ApiResponses({
		@ApiResponse(code=204, message="Serviço rejeitado pelo prestador", response= Problem.class),
	})
	public void recusar (@ApiParam(value = "Id de uma Ordem de Serviço") Long id);
	
	@ApiOperation("Finaliza Ordem de Serviço já realizada")
	@ApiResponses({
		@ApiResponse(code=204, message="Serviço finalizado", response= Problem.class),
	})
	public void finalizar (@ApiParam(value = "Id de uma Ordem de Serviço") Long id);

}

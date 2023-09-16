package com.empreget.api.openApi.controller;

import java.io.IOException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;

import com.empreget.api.dto.FotoPrestadorResponse;
import com.empreget.api.dto.input.FotoPrestadorInput;
import com.empreget.api.exceptionHandler.Problem;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "Foto")
public interface PrestadorFotoControllerOpenApi {

	@ApiOperation("Atualiza foto do prestador")
	@ApiResponses({
		@ApiResponse(code=200, message = "Foto atualizada", response = Problem.class)
	})
	public FotoPrestadorResponse atualizarFoto(@ApiParam(value = "Id de um prestador")Long prestadorId,
			FotoPrestadorInput fotoPrestadorInput) throws IOException;
	
	@ApiOperation("Busca foto por prestador")
	public FotoPrestadorResponse buscar(@ApiParam(value = "Id de um prestador") Long prestadorId);
	
	@ApiOperation("Mostra a foto do prestador registrada no sistema")
	public ResponseEntity<InputStreamResource> mostrarFoto(@ApiParam(value = "Id de um prestador") Long prestadorId, 
			String acceptHeader) throws HttpMediaTypeNotAcceptableException;
	
	@ApiOperation("Exclui foto do prestador")
	@ApiResponse(code=204, message = "Foto excluída do sistema", response = Problem.class)
	public void excluir(@ApiParam(value = "Id de um prestador")Long prestadorId);
}

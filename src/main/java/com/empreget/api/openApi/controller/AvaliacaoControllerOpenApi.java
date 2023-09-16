package com.empreget.api.openApi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import com.empreget.api.dto.AvaliacaoResponse;
import com.empreget.api.dto.input.AvaliacaoInput;
import com.empreget.api.exceptionHandler.Problem;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "Avaliacao")
public interface AvaliacaoControllerOpenApi {
	
	@ApiOperation("Avalia a qualidade do serviço prestado em determinada Ordem de Serviço e respectivo prestador")
	@ApiResponses({
		@ApiResponse(code=201, message="Avaliação realizada", response= Problem.class),
	})
	public AvaliacaoResponse avaliarPrestador(AvaliacaoInput avaliacaoinput, 
			@ApiParam(value = "Id de uma Ordem de Serviço", example="1") Long ordemServicoId);
		
		
	@ApiOperation("Lista avaliações por prestador")
	public Page<AvaliacaoResponse> ListarAvaliacoesPorPrestador(@ApiParam(value = "Id de um prestador")Long prestadorId, 
			Pageable pageable);
	
	@ApiOperation("Lista avaliações por Ordem de Serviço")
	public Page<AvaliacaoResponse> ListarAvaliacoesPorOS(@ApiParam(value = "Id de uma Ordem de Serviço") 
					Long ordemServicoId, Pageable pageable);
		
			
	@ApiOperation("Efetua o cálculo da média das avaliações por prestador")
	public ResponseEntity<Double> calcularMediaAvaliacoesPrestador(@ApiParam(value = "Id de um prestador")Long prestadorId);

		
	@ApiOperation("Efetua o cálculo da quantidade de avaliações recebidas por prestador")
	public ResponseEntity<Long> calcularQuantidadeAvaliacoesPrestador(Long prestadorId);

}

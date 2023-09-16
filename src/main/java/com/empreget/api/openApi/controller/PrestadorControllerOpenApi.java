package com.empreget.api.openApi.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.empreget.api.dto.OrdemServicoDataResponse;
import com.empreget.api.dto.PrestadorFiltroRegiaoResponse;
import com.empreget.api.dto.PrestadorMinResponse;
import com.empreget.api.dto.PrestadorResponse;
import com.empreget.api.dto.input.PrestadorInput;
import com.empreget.api.exceptionHandler.Problem;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "Prestadores")
public interface PrestadorControllerOpenApi {

	@ApiOperation("Lista prestadores")
	public Page<PrestadorResponse> listar(Pageable pageable);


	@ApiOperation("Lista prestadores por região")
	public Page<PrestadorFiltroRegiaoResponse> listarPorRegiao(@ApiParam(value = "Região que o prestador atende")String regiao,
			 Pageable pageable);


	@ApiOperation("Lista prestadores por nome")
	public Page<PrestadorFiltroRegiaoResponse> ListarPorNomeContem(@ApiParam(value = "Nome do prestador") String nome, Pageable pageable);
	

	@ApiOperation("Lista todos os prestadores com informações essenciais para filtro")
	public Page<PrestadorFiltroRegiaoResponse> listarTodosNoFiltro(Pageable pageable);


	@ApiOperation("Busca prestador por Id com informações essenciais")
	@ApiResponses({
		@ApiResponse(code=400, message="Código com parâmetro inválido", response= Problem.class),
		@ApiResponse(code=404, message="Prestador não encontrado", response= Problem.class)
	})
	public PrestadorMinResponse buscarPerfilPorId(@ApiParam(value = "Id de um prestador") Long prestadorId);


	@ApiOperation("Busca Ordem de Serviço por data do serviço")
	@ApiResponses({
		@ApiResponse(code=400, message="Data com parâmetros inválidos", response= Problem.class)
	})
	public List<OrdemServicoDataResponse> buscarPorDataServico(@ApiParam(value = "Id de um prestador") Long prestadorId,
			@ApiParam(value = "Data do serviço solicitado") LocalDate dataServico);
	

	@ApiOperation("Busca prestador por Id")
	@ApiResponses({
		@ApiResponse(code=400, message="Código com parâmetro inválido", response= Problem.class),
		@ApiResponse(code=404, message="Prestador não encontrado", response= Problem.class)
	})
	public PrestadorResponse buscarPorId(@ApiParam(value = "Id de um prestador")Long prestadorId);
	
	@ApiOperation("Busca prestador por Id com informações essenciais")
	public PrestadorMinResponse buscarPorIdPerfil(@ApiParam(value = "Id de um prestador")Long prestadorId);
	

	@ApiOperation("Cadastra prestador")
	@ApiResponses({
		@ApiResponse(code=200, message="Prestador cadastrado", response= Problem.class)
	})
	public PrestadorResponse adicionar(PrestadorInput prestadorInput); 

		
	@ApiOperation("Edita prestador")
	@ApiResponses({
		@ApiResponse(code=200, message="cadastro editado", response= Problem.class)
	})
	public PrestadorResponse editar(@ApiParam(value = "Id de um prestador") Long prestadorId, PrestadorInput prestadorInput);


	@ApiOperation("Exclui prestador")
	@ApiResponses({
		@ApiResponse(code=204, message="Cadastro de prestador excluído do sistema", response= Problem.class)
	})
	public void excluir(@ApiParam(value = "Id de um prestador") Long prestadorId);

}

package com.empreget.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.empreget.api.assembler.PrestadorDtoAssembler;
import com.empreget.api.assembler.PrestadorInputDisassembler;
import com.empreget.api.dto.PrestadorFiltroRegiaoResponse;
import com.empreget.api.dto.PrestadorMinResponse;
import com.empreget.api.dto.PrestadorResponse;
import com.empreget.api.dto.input.PrestadorInput;
import com.empreget.domain.model.Prestador;
import com.empreget.domain.model.enums.Regiao;
import com.empreget.domain.repository.PrestadorRepository;
import com.empreget.domain.service.CatalogoPrestadorService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/prestadores")
public class PrestadorController {

	private PrestadorRepository prestadorRepository;
	private CatalogoPrestadorService catalogoPrestadorService;
	private PrestadorDtoAssembler prestadorDtoAssembler;
	private PrestadorInputDisassembler prestadorInputDisassembler;

	@GetMapping
	public List<PrestadorResponse> listar(){
		return prestadorRepository.findAll()
				.stream()
				.map(prestador -> prestadorDtoAssembler.toModel(prestador))
				.collect(Collectors.toList());
	}
	
	@GetMapping("/regiao/{regiao}")
	public List<PrestadorFiltroRegiaoResponse> listarPorRegiao(@PathVariable String regiao){
		Regiao regiaoEnum = Regiao.valueOf(regiao.toUpperCase());
				
		return  prestadorDtoAssembler.toCollectionMinFilterModel(catalogoPrestadorService.obterPrestadoresPorRegiao(regiaoEnum));
	}

		  
	@GetMapping ("/perfis")
	public List<PrestadorMinResponse> listarPerfilPrestador(){
		return prestadorRepository.findAll()
				.stream()
				.map(prestador -> prestadorDtoAssembler.toModelMin(prestador))
				.collect(Collectors.toList());
				
	}

	@GetMapping("/nome-contem/{nome}")
	public List<PrestadorFiltroRegiaoResponse> buscarPorNomeContem(@PathVariable String nome) {
		return prestadorDtoAssembler.toCollectionMinFilterModel(catalogoPrestadorService.buscarPorNomeContem(nome));
	}
	
	@GetMapping("/{prestadorId}")
	public PrestadorResponse buscarPorId(@PathVariable Long prestadorId){
		return prestadorDtoAssembler.toModel(catalogoPrestadorService.buscarOuFalhar(prestadorId));
		
	}
	@GetMapping("/perfil/{prestadorId}")
	public PrestadorMinResponse buscarPorIdPerfil(@PathVariable Long prestadorId){
		return prestadorDtoAssembler.toModelMin(catalogoPrestadorService.buscarOuFalhar(prestadorId));
		
	}
		
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PrestadorResponse adicionar(@Valid @RequestBody PrestadorInput prestadorInput) {
		
		Prestador prestador = prestadorInputDisassembler.toDomainObject(prestadorInput);
		return prestadorDtoAssembler.toModel(catalogoPrestadorService.salvar(prestador));
		
	}
	
	@PutMapping("/{prestadorId}")
	public PrestadorResponse editar(@PathVariable Long prestadorId, @RequestBody @Valid PrestadorInput prestadorInput) {
	
			Prestador prestadorAtual = catalogoPrestadorService.buscarOuFalhar(prestadorId);
			
			prestadorInputDisassembler.copyToDomainObjet(prestadorInput, prestadorAtual);
			
			return prestadorDtoAssembler.toModel(catalogoPrestadorService.salvar(prestadorAtual));
	
	}

//NÃO FAZ SENTIDO TER EDITAR PARCIAL COM DTO
//	@PatchMapping("/{prestadorId}")
//	public PrestadorResponse editarParcial(@PathVariable Long prestadorId, @Valid @RequestBody Map<String, Object> dados,
//			HttpServletRequest request) {
//		
//		Prestador prestadorAtual = catalogoPrestadorService.buscarOuFalhar(prestadorId);
//		
//		merge(dados, prestadorAtual, request);
//		
//		return prestadorAssembler.toModel(catalogoPrestadorService.salvar(prestadorAtual));
//	}
//	
//	private void merge(Map<String, Object> dadosOrigem, Prestador prestadorDestino, HttpServletRequest request) {
//		ServletServerHttpRequest serverHttpRequest = new ServletServerHttpRequest(request);
//
//		try {
//			ObjectMapper objectMapper = new ObjectMapper();
//			objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
//			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
//
//			Prestador prestadorOrigem = objectMapper.convertValue(dadosOrigem, Prestador.class);
//
//			dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
//				Field field = ReflectionUtils.findField(Prestador.class, nomePropriedade);
//				field.setAccessible(true);
//
//				Object novoValor = ReflectionUtils.getField(field, prestadorOrigem);
//
//				ReflectionUtils.setField(field, prestadorDestino, novoValor);
//			});
//		} catch (IllegalArgumentException e) {
//			Throwable rootCause = ExceptionUtils.getRootCause(e);
//			throw new HttpMessageNotReadableException(e.getMessage(), rootCause, serverHttpRequest);
//		}
//	}
	
	
	@DeleteMapping("/{prestadorId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void excluir (@PathVariable Long prestadorId){		
		catalogoPrestadorService.excluir(prestadorId);
		
	}
	
	
	
	
}

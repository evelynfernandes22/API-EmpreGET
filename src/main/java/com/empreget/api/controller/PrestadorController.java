package com.empreget.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.empreget.domain.model.Prestador;
import com.empreget.domain.repository.PrestadorRepository;
import com.empreget.domain.service.catalogoPrestadorService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/prestadores")
public class PrestadorController {

	private PrestadorRepository prestadorRepository;

	private catalogoPrestadorService catalogoPrestadorService;

	@GetMapping
	public List<Prestador> listar(){
		return prestadorRepository.findAll();
	}
	
	@GetMapping("/{prestadorId}")
	public Prestador buscar(@PathVariable Long prestadorId){
		return catalogoPrestadorService.buscarOuFalhar(prestadorId);
		
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Prestador adicionar(@Valid @RequestBody Prestador prestador) {
		return catalogoPrestadorService.salvar(prestador);
	}
	
	@PutMapping("/{prestadorId}")
	public Prestador editar(@PathVariable Long prestadorId, @RequestBody Prestador prestador) {
		Prestador prestadorAtual = catalogoPrestadorService.buscarOuFalhar(prestadorId);
		
		BeanUtils.copyProperties(prestador, prestadorAtual, "id", "dataDoCadastro", "dataDaAtualizacao");
		
		return catalogoPrestadorService.salvar(prestadorAtual);
	}
	
	@PatchMapping("/{prestadorId}")
	public Prestador editarParcial(@PathVariable Long prestadorId, @RequestBody Map<String, Object> dados) {
		
		Prestador prestadorAtual = catalogoPrestadorService.buscarOuFalhar(prestadorId);
		
		merge(dados, prestadorAtual);
		
		return editar(prestadorId, prestadorAtual);
	}
	
	private void merge(Map<String, Object> dadosOrigem, Prestador prestadorDestino) {
		ObjectMapper objectMapper = new ObjectMapper();
		Prestador prestadorOrigem = objectMapper.convertValue(dadosOrigem, Prestador.class);
		
		dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
			Field field = ReflectionUtils.findField(Prestador.class, nomePropriedade);
			field.setAccessible(true);
			
			Object novoValor = ReflectionUtils.getField(field, prestadorOrigem);
			
			ReflectionUtils.setField(field, prestadorDestino, novoValor);
		});
	}

	@DeleteMapping("/{prestadorId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void excluir (@PathVariable Long prestadorId){		
		catalogoPrestadorService.excluir(prestadorId);
		
	}
}

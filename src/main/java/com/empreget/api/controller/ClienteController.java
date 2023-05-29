package com.empreget.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
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

import com.empreget.api.assembler.ClienteAssembler;
import com.empreget.api.assembler.ClienteInputDisassembler;
import com.empreget.api.dto.ClienteResponse;
import com.empreget.api.dto.input.ClienteInput;
import com.empreget.domain.model.Cliente;
import com.empreget.domain.repository.ClienteRepository;
import com.empreget.domain.service.CatalogoClienteService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/clientes")
public class ClienteController {

	private final ClienteRepository clienteRepository;
	private final CatalogoClienteService catalogoClienteService;
	private ClienteAssembler clienteAssembler;
	private ClienteInputDisassembler clienteInputDisassembler;


	@GetMapping
	public List<ClienteResponse> listar() {
		return clienteRepository.findAll()
				.stream()
				.map(cliente -> clienteAssembler.toModel(cliente))
				.collect(Collectors.toList());

	}
	
	@GetMapping("/{clienteId}")
	public ClienteResponse buscarPorId(@PathVariable Long clienteId) {
		return clienteAssembler.toModel(catalogoClienteService.buscarOuFalhar(clienteId));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ClienteResponse adicionar(@Valid @RequestBody ClienteInput clienteInput) {
		
		Cliente cliente = clienteInputDisassembler.toDomainObject(clienteInput);
		return clienteAssembler.toModel(catalogoClienteService.salvar(cliente));
			
	}

	@PutMapping("/{clienteId}")
	public ClienteResponse editar(@PathVariable Long clienteId, @Valid @RequestBody ClienteInput clienteinput) {
		
		Cliente cliente = clienteInputDisassembler.toDomainObject(clienteinput);
		
		Cliente clienteAtual = catalogoClienteService.buscarOuFalhar(clienteId);
			
		BeanUtils.copyProperties(cliente, clienteAtual,
				"id", "dataDoCadastro", "dataDaAtualizacao");
			
		return clienteAssembler.toModel(catalogoClienteService.salvar(clienteAtual));
		
	}

	@PatchMapping("/{clienteId}")
	public ClienteResponse editarParcial (@PathVariable Long clienteId, @Valid @RequestBody Map<String, Object> dados, 
			HttpServletRequest request) {
		
		Cliente clienteAtual = catalogoClienteService.buscarOuFalhar(clienteId);
		
		merge(dados, clienteAtual, request);
		
		return clienteAssembler.toModel(catalogoClienteService.salvar(clienteAtual));
	}
	
	
	private void merge(Map<String, Object> dadosOrigem, Cliente clienteDestino,
			HttpServletRequest request) {
		ServletServerHttpRequest serverHttpRequest = new ServletServerHttpRequest(request);
		
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
			
			Cliente clienteOrigem = objectMapper.convertValue(dadosOrigem, Cliente.class);
			
			dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
				Field field = ReflectionUtils.findField(Cliente.class, nomePropriedade);
				field.setAccessible(true);
				
				Object novoValor = ReflectionUtils.getField(field, clienteOrigem);
				
				ReflectionUtils.setField(field, clienteDestino, novoValor);
			});
		} catch (IllegalArgumentException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			throw new HttpMessageNotReadableException(e.getMessage(), rootCause, serverHttpRequest);
		}
	}
	
//	private void merge(Map<String, Object> dadosOrigem, Cliente clienteDestino) {
//		ObjectMapper objectMapper = new ObjectMapper();
//		Cliente clienteOrigem = objectMapper.convertValue(dadosOrigem, Cliente.class);
//		
//		dadosOrigem.forEach((nomePropriedade, valorPropriedade)-> {
//			Field field = ReflectionUtils.findField(Cliente.class, nomePropriedade);
//			field.setAccessible(true);
//				
//			Object novoValor = ReflectionUtils.getField(field, clienteOrigem);
//			
//			ReflectionUtils.setField(field, clienteDestino, novoValor);
//			
//		});
//	}
	
//	ATUALIZAÇÃO PARCIAL, VER COMO CONVERTER PARA DTO COM MODELMAPPER
//	@PatchMapping("/{clienteId}")
//	public Cliente editarParcial (@PathVariable Long clienteId, @Valid @RequestBody Map<String, Object> dados) {
//		
//		Cliente clienteAtual = catalogoClienteService.buscarOuFalhar(clienteId);
//		
//		merge(dados, clienteAtual);
//		
//		return editar(clienteId, clienteAtual);
//	}
//	
//	private void merge(Map<String, Object> dadosOrigem, Cliente clienteDestino) {
//		ObjectMapper objectMapper = new ObjectMapper();
//		Cliente clienteOrigem = objectMapper.convertValue(dadosOrigem, Cliente.class);
//		
//		dadosOrigem.forEach((nomePropriedade, valorPropriedade)-> {
//			Field field = ReflectionUtils.findField(Cliente.class, nomePropriedade);
//			field.setAccessible(true);
//				
//			Object novoValor = ReflectionUtils.getField(field, clienteOrigem);
//			
//			ReflectionUtils.setField(field, clienteDestino, novoValor);
//			
//		});
//	}
	
	@DeleteMapping("/{clienteId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long clienteId) {
		catalogoClienteService.excluir(clienteId);			
	}
}

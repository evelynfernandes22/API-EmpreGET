package com.empreget.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.http.HttpStatus;
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

import com.empreget.domain.exception.ClienteNaoEncontradoException;
import com.empreget.domain.exception.NegocioException;
import com.empreget.domain.model.Cliente;
import com.empreget.domain.repository.ClienteRepository;
import com.empreget.domain.service.CatalogoClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/clientes")
public class ClienteController {

	private final ClienteRepository clienteRepository;

	private final CatalogoClienteService catalogoClienteService;


	@GetMapping
	public List<Cliente> listar() {
		return clienteRepository.findAll();

	}
	
	@GetMapping("/{clienteId}")
	public Cliente buscarPorId(@PathVariable Long clienteId) {
		return catalogoClienteService.buscarOuFalhar(clienteId);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Cliente adicionar(@Valid @RequestBody Cliente cliente) {
		return catalogoClienteService.salvar(cliente);
			
	}

	@PutMapping("/{clienteId}")
	public Cliente editar(@PathVariable Long clienteId, @Valid @RequestBody Cliente cliente) {
		try {
			Cliente clienteAtual = catalogoClienteService.buscarOuFalhar(clienteId);
			
			BeanUtils.copyProperties(cliente, clienteAtual, "id", "dataDoCadastro", "dataDaAtualizacao");
			
			return catalogoClienteService.salvar(clienteAtual);
			
		} catch (ClienteNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}
	
	@PatchMapping("/{clienteId}")
	public Cliente editarParcial (@PathVariable Long clienteId, @RequestBody Map<String, Object> dados) {
		
		Cliente clienteAtual = catalogoClienteService.buscarOuFalhar(clienteId);
		
		merge(dados, clienteAtual);
		
		return editar(clienteId, clienteAtual);
	}
	
	private void merge(Map<String, Object> dadosOrigem, Cliente clienteDestino) {
		ObjectMapper objectMapper = new ObjectMapper();
		Cliente clienteOrigem = objectMapper.convertValue(dadosOrigem, Cliente.class);
		
		dadosOrigem.forEach((nomePropriedade, valorPropriedade)-> {
			Field field = ReflectionUtils.findField(Cliente.class, nomePropriedade);
			field.setAccessible(true);
				
			Object novoValor = ReflectionUtils.getField(field, clienteOrigem);
			
			ReflectionUtils.setField(field, clienteDestino, novoValor);
			
		});
	}

// 	ANTES DA REFATORAÇÃO	
//	@DeleteMapping("/{clienteId}")
//	public ResponseEntity<Void> excluir(@PathVariable Long clienteId) {
//
//		try {
//			if (clienteRepository.existsById(clienteId)) {
//				catalogoClienteService.excluir(clienteId);
//			}
//			return ResponseEntity.noContent().build();
//
//		} catch (EntidadeNaoEncontradaException e) {
//			return ResponseEntity.notFound().build();
//
//		} catch (EntidadeEmUsoException e) {
//			return ResponseEntity.status(HttpStatus.CONFLICT).build();
//		}
//	}
	
	@DeleteMapping("/{clienteId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long clienteId) {
		catalogoClienteService.excluir(clienteId);			
	}
}

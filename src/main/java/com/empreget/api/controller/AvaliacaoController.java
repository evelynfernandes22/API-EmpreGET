package com.empreget.api.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.empreget.api.assembler.AvaliacaoDtoAssembler;
import com.empreget.api.assembler.AvaliacaoInputDisassembler;
import com.empreget.api.dto.AvaliacaoResponse;
import com.empreget.api.dto.input.AvaliacaoInput;
import com.empreget.domain.exception.ClienteNaoEncontradoException;
import com.empreget.domain.model.Avaliacao;
import com.empreget.domain.model.Cliente;
import com.empreget.domain.repository.ClienteRepository;
import com.empreget.domain.service.AvaliacaoService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

	private AvaliacaoService avaliacaoService;
	private AvaliacaoInputDisassembler avaliacaoInputDisassembler;
	private ClienteRepository clienteRepository;
	private AvaliacaoDtoAssembler avaliacaoDtoAssembler;
	
	
	@PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public AvaliacaoResponse avaliarPrestador(@Valid @RequestBody AvaliacaoInput avaliacaoinput) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String emailCliente = authentication.getName();
		
		Cliente cliente = clienteRepository.findByUsuarioEmail(emailCliente)
				.orElseThrow(() -> new ClienteNaoEncontradoException("Cliente não encontrado registrado com o email " + emailCliente));
		
		Avaliacao avaliacao = avaliacaoInputDisassembler.toDomainObject(avaliacaoinput);
		avaliacao.setCliente(cliente);
		
		return  avaliacaoDtoAssembler.toModel(avaliacaoService.avaliar(avaliacao));
	}
	
	@GetMapping("/media/{prestadorId}")
	public ResponseEntity<Double> calcularMediaAvaliacoesPrestador(@PathVariable Long prestadorId){
		
		double mediaAvaliacoes = avaliacaoService.calcularMediaAvaliacoes(prestadorId);
		return ResponseEntity.ok(mediaAvaliacoes); 
	}
		
	@GetMapping("/quantidade/{prestadorId}")
	public ResponseEntity<Long> calcularQuantidadeAvaliacoesPrestador(@PathVariable Long prestadorId) {
		
		long quantidadeAvaliacoes = avaliacaoService.calcularQuantidadeAvaliacoes(prestadorId);
		return ResponseEntity.ok(quantidadeAvaliacoes);
	}

}

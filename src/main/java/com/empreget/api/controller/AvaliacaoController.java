package com.empreget.api.controller;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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
import com.empreget.domain.exception.NegocioException;
import com.empreget.domain.model.Avaliacao;
import com.empreget.domain.model.Cliente;
import com.empreget.domain.model.Prestador;
import com.empreget.domain.repository.AvaliacaoRepository;
import com.empreget.domain.repository.ClienteRepository;
import com.empreget.domain.repository.PrestadorRepository;
import com.empreget.domain.service.AvaliacaoService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

	private AvaliacaoService avaliacaoService;
	private AvaliacaoRepository avaliacaoRepository;
	private AvaliacaoInputDisassembler avaliacaoInputDisassembler;
	private ClienteRepository clienteRepository;
	private AvaliacaoDtoAssembler avaliacaoDtoAssembler;
	private PrestadorRepository prestadorRepository;
	
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
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
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE', 'PRESTADOR')")
	@GetMapping("/{prestadorId}")
	public Page<AvaliacaoResponse> ListarAvaliacoesPorPrestador(@PathVariable Long prestadorId, @PageableDefault(size = 10) Pageable pageable){
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		List<String> roles = authentication.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());
		
		if(roles.contains("ROLE_ADMIN")) {
			Page<Avaliacao> prestadorPage = avaliacaoRepository.findByPrestadorId(prestadorId, pageable);
			return prestadorPage.map(prestador -> avaliacaoDtoAssembler.toModel(prestador));
			
		}else if (roles.contains("ROLE_CLIENTE")) {
			String emailUser = authentication.getName();
	        Cliente cliente = clienteRepository.findByUsuarioEmail(emailUser)
	                .orElseThrow(() -> new NegocioException("Cliente não encontrado."));
			    
	        Page<Avaliacao> clientePage = avaliacaoRepository.findByPrestadorIdAndClienteId(prestadorId, 
	        		cliente.getId(), pageable);
	       
	        if (!clientePage.isEmpty()) {
	            return clientePage.map(avaliacaoDtoAssembler::toModel);
	        } else {
	            throw new NegocioException("Não há avaliações realizadas por você a este prestador.");
	        }
	    }else if(roles.contains("ROLE_PRESTADOR")) {
			String emailUser = authentication.getName();
			Prestador prestador = prestadorRepository.findByUsuarioEmail(emailUser)
					.orElseThrow(() -> new NegocioException("Prestador não encontrado."));
			
			if(!prestador.getId().equals(prestadorId)) {
				throw new AccessDeniedException("Acesso negado: Você não tem permissão para visualizar "
						+ "avaliações de outros prestadores.");
			}
			
			Page<Avaliacao> prestadorPage = avaliacaoRepository.findByPrestadorId(prestador.getId(), pageable);
			return prestadorPage.map(avaliacao -> avaliacaoDtoAssembler.toModel(avaliacao));
		}
		return new PageImpl<>(Collections.emptyList());
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

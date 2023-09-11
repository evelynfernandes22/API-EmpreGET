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
import com.empreget.domain.exception.AcessoNegadoException;
import com.empreget.domain.exception.NegocioException;
import com.empreget.domain.model.Avaliacao;
import com.empreget.domain.model.Cliente;
import com.empreget.domain.model.OrdemServico;
import com.empreget.domain.model.Prestador;
import com.empreget.domain.repository.AvaliacaoRepository;
import com.empreget.domain.repository.ClienteRepository;
import com.empreget.domain.repository.PrestadorRepository;
import com.empreget.domain.service.AvaliacaoService;
import com.empreget.domain.service.BuscaOSService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;

@Api(tags = "Avaliacao")
@AllArgsConstructor
@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

	private AvaliacaoService avaliacaoService;
	private AvaliacaoRepository avaliacaoRepository;
	private AvaliacaoInputDisassembler avaliacaoInputDisassembler;
	private ClienteRepository clienteRepository;
	private BuscaOSService buscaOS;
	private AvaliacaoDtoAssembler avaliacaoDtoAssembler;
	private PrestadorRepository prestadorRepository;
	
	@ApiOperation("Avalia a qualidade do serviço prestado em determinada Ordem de Serviço e respectivo prestador")
	@PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
	@PostMapping("/os/{ordemServicoId}")
	@ResponseStatus(HttpStatus.CREATED)
	public AvaliacaoResponse avaliarPrestador(@Valid @RequestBody AvaliacaoInput avaliacaoinput, 
			@ApiParam(value = "Id de uma Ordem de Serviço", example="1") @PathVariable @Valid Long ordemServicoId) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String emailCliente = authentication.getName();
		
		OrdemServico os = buscaOS.buscarOuFalhar(ordemServicoId);  
		Cliente cliente = os.getCliente();
		Prestador prestador = os.getPrestador();
		
		if (!cliente.getUsuario().getEmail().equals(emailCliente)) {
	        throw new AcessoNegadoException("Cliente não autorizado para avaliar esta ordem de serviço.");
	    }
		
		Avaliacao avaliacao = avaliacaoInputDisassembler.toDomainObject(avaliacaoinput);
		avaliacao.setCliente(cliente);
		avaliacao.setPrestador(prestador);
		avaliacao.setOrdemServico(os);
		
		return  avaliacaoDtoAssembler.toModel(avaliacaoService.avaliar(avaliacao));
	}
	
	@ApiOperation("Lista avaliações por prestador")
	@PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE', 'PRESTADOR')")
	@GetMapping("/prestador/{prestadorId}")
	public Page<AvaliacaoResponse> ListarAvaliacoesPorPrestador(@ApiParam(value = "Id de um prestador") @PathVariable Long prestadorId, 
			@PageableDefault(size = 10) Pageable pageable){
		
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
	
	@ApiOperation("Lista avaliações por Ordem de Serviço")
	@PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE', 'PRESTADOR')")
	@GetMapping("/os/{ordemServicoId}")
	public Page<AvaliacaoResponse> ListarAvaliacoesPorOS(@ApiParam(value = "Id de uma Ordem de Serviço") 
					@PathVariable Long ordemServicoId, @PageableDefault(size = 10) Pageable pageable){
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		List<String> roles = authentication.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());
		
		if(roles.contains("ROLE_ADMIN")) {
			Page<Avaliacao> osPage = avaliacaoRepository.findByOrdemServicoId(ordemServicoId, pageable);
			return osPage.map(os -> avaliacaoDtoAssembler.toModel(os));
			
		}else if (roles.contains("ROLE_CLIENTE")) {
			String emailUser = authentication.getName();
			Cliente cliente = clienteRepository.findByUsuarioEmail(emailUser)
					.orElseThrow(() -> new NegocioException("Cliente não encontrado."));
			
			Page<Avaliacao> clientePage = avaliacaoRepository.findByOrdemServicoIdAndClienteId(ordemServicoId, 
					cliente.getId(), pageable);
			
			if (!clientePage.isEmpty()) {
				return clientePage.map(avaliacaoDtoAssembler::toModel);
			} else {
				throw new NegocioException("Não há avaliações realizadas por você nesta Ordem de Serviço.");
			}
		}else if(roles.contains("ROLE_PRESTADOR")) {
			String emailUser = authentication.getName();
			Prestador prestador = prestadorRepository.findByUsuarioEmail(emailUser)
					.orElseThrow(() -> new NegocioException("Prestador não encontrado."));
			
			OrdemServico os = buscaOS.buscarOuFalhar(ordemServicoId);
			
			if(!prestador.getId().equals(os.getPrestador().getId())) {
				throw new AccessDeniedException("Acesso negado: Você não tem permissão para visualizar "
						+ "avaliações de outros prestadores.");
			}
			
			Page<Avaliacao> osPage = avaliacaoRepository.findByOrdemServicoId(os.getId(), pageable);
			return osPage.map(avaliacao -> avaliacaoDtoAssembler.toModel(avaliacao));
		}
		return new PageImpl<>(Collections.emptyList());
	}
	
	@ApiOperation("Efetua o cálculo da média das avaliações por prestador")
	@GetMapping("/media/{prestadorId}")
	public ResponseEntity<Double> calcularMediaAvaliacoesPrestador(@ApiParam(value = "Id de um prestador") @PathVariable Long prestadorId){
		
		double mediaAvaliacoes = avaliacaoService.calcularMediaAvaliacoes(prestadorId);
		return ResponseEntity.ok(mediaAvaliacoes); 
	}
		
	@ApiOperation("Efetua o cálculo da quantidade de avaliações recebidas por prestador")
	@GetMapping("/quantidade/{prestadorId}")
	public ResponseEntity<Long> calcularQuantidadeAvaliacoesPrestador(@PathVariable Long prestadorId) {
		
		long quantidadeAvaliacoes = avaliacaoService.calcularQuantidadeAvaliacoes(prestadorId);
		return ResponseEntity.ok(quantidadeAvaliacoes);
	}

}

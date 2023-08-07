package com.empreget.api.controller;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.empreget.api.assembler.OrdemServicoDtoAssembler;
import com.empreget.api.assembler.PrestadorDtoAssembler;
import com.empreget.api.assembler.PrestadorInputDisassembler;
import com.empreget.api.dto.OrdemServicoDataResponse;
import com.empreget.api.dto.PrestadorFiltroRegiaoResponse;
import com.empreget.api.dto.PrestadorMinResponse;
import com.empreget.api.dto.PrestadorResponse;
import com.empreget.api.dto.input.PrestadorInput;
import com.empreget.domain.exception.NegocioException;
import com.empreget.domain.model.Prestador;
import com.empreget.domain.model.Usuario;
import com.empreget.domain.model.enums.Regiao;
import com.empreget.domain.model.enums.UserRole;
import com.empreget.domain.repository.PrestadorRepository;
import com.empreget.domain.service.CadastroUsuarioService;
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
	private CadastroUsuarioService cadastroUsuarioService;
	private OrdemServicoDtoAssembler ordemServicoDtoAssembler;

//Lista com dados completos
		@PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE', 'PRESTADOR')")
		@GetMapping
		public List<PrestadorResponse> listar(){
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			List<String> roles = authentication.getAuthorities()
					.stream()
					.map(GrantedAuthority::getAuthority)
					.collect(Collectors.toList());
			
			if(roles.contains("ROLE_ADMIN") || roles.contains("ROLE_CLIENTE")) {
				return prestadorRepository.findAll()
						.stream()
						.map(prestador -> prestadorDtoAssembler.toModel(prestador))
						.collect(Collectors.toList());
			}else if(roles.contains("ROLE_PRESTADOR")) {
				String emailUser = authentication.getName();
				Prestador prestador = prestadorRepository.findByUsuarioEmail(emailUser)
						.orElseThrow(() -> new NegocioException("Prestador não encontrado."));
				return Collections.singletonList(prestadorDtoAssembler.toModel(prestador));
			}
			return Collections.emptyList();
		}	
	
//TELA HOME
	@PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
	@GetMapping("/regiao/{regiao}")
	public List<PrestadorFiltroRegiaoResponse> listarPorRegiao(@PathVariable String regiao){
		
			Regiao regiaoEnum = Regiao.valueOf(regiao.toUpperCase());
			return  prestadorDtoAssembler.toCollectionMinFilterModel(catalogoPrestadorService.obterPrestadoresPorRegiao(regiaoEnum));
	}
	
//TELA HOME
	@PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
	@GetMapping("/nome-contem/{nome}")
	public List<PrestadorFiltroRegiaoResponse> buscarPorNomeContem(@PathVariable String nome) {
		return prestadorDtoAssembler.toCollectionMinFilterModel(catalogoPrestadorService.buscarPorNomeContem(nome));
	}
	
	
//TELA HOME - lista todos de forma mínima para ser utilizado no catálogo
	@PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE', 'PRESTADOR')")
	@GetMapping("/filtro")
	public List<PrestadorFiltroRegiaoResponse> listarTodosNoFiltro(){
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		List<String> roles = authentication.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());
		
		if(roles.contains("ROLE_ADMIN") || roles.contains("ROLE_CLIENTE")) {
			return prestadorRepository.findAll()
					.stream()
					.map(prestador -> prestadorDtoAssembler.toModelMinFilter(prestador))
					.collect(Collectors.toList());
		}else if(roles.contains("ROLE_PRESTADOR")) {
			String emailUser = authentication.getName();
			Prestador prestador = prestadorRepository.findByUsuarioEmail(emailUser)
					.orElseThrow(() -> new NegocioException("Prestador não encontrado."));
			return Collections.singletonList(prestadorDtoAssembler.toModelMinFilter(prestador));
		}
		return Collections.emptyList();
	}	
	
//TELA DETALHES DO PERFIL
	@PreAuthorize("@acessoService.verificarAcessoProprioPrestador(#prestadorId) or hasAnyRole('ADMIN', 'CLIENTE')")
	@GetMapping("/perfis/{prestadorId}")
	public PrestadorMinResponse buscarPerfilPorId(@PathVariable Long prestadorId){
		return prestadorDtoAssembler.toModelMin(catalogoPrestadorService.buscarOuFalhar(prestadorId));
	}
	
	//Remover caso não seja necessário...
	@PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")	  
	@GetMapping ("/perfis")
	public List<PrestadorMinResponse> listarPerfilPrestador(){
		return prestadorRepository.findAll()
				.stream()
				.map(prestador -> prestadorDtoAssembler.toModelMin(prestador))
				.collect(Collectors.toList());
	}

	
// TELA PRESTADOR
	@PreAuthorize("hasAnyRole('ADMIN', 'PRESTADOR')")
	@GetMapping("/{prestadorId}/ordens-servico")
	public List<OrdemServicoDataResponse> buscarPorDataServico(@PathVariable Long prestadorId, 
			@RequestParam("data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataServico){
		return ordemServicoDtoAssembler
				.toCollectionOSDataModel(catalogoPrestadorService.buscarOrdensServicoPorDataServico(prestadorId, dataServico));
	}
	
	
	@PreAuthorize("@acessoService.verificarAcessoProprioPrestador(#prestadorId) or hasAnyRole('ADMIN', 'CLIENTE')")
	@GetMapping("/{prestadorId}")
	public PrestadorResponse buscarPorId(@PathVariable Long prestadorId){
		return prestadorDtoAssembler.toModel(catalogoPrestadorService.buscarOuFalhar(prestadorId));
	}
	
	@PreAuthorize("@acessoService.verificarAcessoProprioPrestador(#prestadorId) or hasAnyRole('ADMIN', 'CLIENTE')")
	@GetMapping("/perfil/{prestadorId}")
	public PrestadorMinResponse buscarPorIdPerfil(@PathVariable Long prestadorId){
		return prestadorDtoAssembler.toModelMin(catalogoPrestadorService.buscarOuFalhar(prestadorId));
	}
	
//TELA CADASTRO
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PrestadorResponse adicionar(@Valid @RequestBody PrestadorInput prestadorInput) {
		
		Prestador prestador = prestadorInputDisassembler.toDomainObject(prestadorInput);
		
		Usuario usuario = new Usuario();
		usuario.setEmail(prestador.getUsuario().getEmail());
		usuario.setSenha(prestador.getUsuario().getSenha());
		prestador.getUsuario().getRole();
		usuario.setRole(UserRole.PRESTADOR);
		usuario.setNome(prestador.getNome());
		
		Usuario usuarioSalvo = cadastroUsuarioService.cadastrarUser(usuario);
		prestador.setUsuario(usuarioSalvo);
		
		return prestadorDtoAssembler.toModel(catalogoPrestadorService.salvar(prestador));
		
	}
//TELA EDITAR	
	@PreAuthorize("@acessoService.verificarAcessoProprioPrestador(#prestadorId) or hasRole('ADMIN')")
	@PutMapping("/{prestadorId}")
	public PrestadorResponse editar(@PathVariable Long prestadorId, @RequestBody @Valid PrestadorInput prestadorInput) {
			
		Prestador prestador = prestadorInputDisassembler.toDomainObject(prestadorInput);
		Prestador prestadorAtual = catalogoPrestadorService.buscarOuFalhar(prestadorId);
		
		BeanUtils.copyProperties(prestador, prestadorAtual, 
				"id", "dataDoCadastro", "dataDaAtualizacao", "usuario");
	
		String nomeAtual = prestadorAtual.getNome();
		prestadorAtual.getUsuario().setNome(nomeAtual);
			
		return prestadorDtoAssembler.toModel(catalogoPrestadorService.salvar(prestadorAtual));
	
	}
	
//Exclusivo para a tela do admin
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{prestadorId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void excluir (@PathVariable Long prestadorId){		
		catalogoPrestadorService.excluir(prestadorId);
		
	}


	
}

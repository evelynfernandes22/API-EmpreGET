package com.empreget.api.controller;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.empreget.api.openApi.controller.PrestadorControllerOpenApi;
import com.empreget.domain.exception.NegocioException;
import com.empreget.domain.model.Prestador;
import com.empreget.domain.model.Usuario;
import com.empreget.domain.model.enums.Regiao;
import com.empreget.domain.model.enums.UserRole;
import com.empreget.domain.repository.PrestadorRepository;
import com.empreget.domain.service.CadastroUsuarioService;
import com.empreget.domain.service.CatalogoPrestadorService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/prestadores", produces = MediaType.APPLICATION_JSON_VALUE)
public class PrestadorController implements PrestadorControllerOpenApi {

	private PrestadorRepository prestadorRepository;
	private CatalogoPrestadorService catalogoPrestadorService;
	private PrestadorDtoAssembler prestadorDtoAssembler;
	private PrestadorInputDisassembler prestadorInputDisassembler;
	private CadastroUsuarioService cadastroUsuarioService;
	private OrdemServicoDtoAssembler ordemServicoDtoAssembler;

	@PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE', 'PRESTADOR')")
	@GetMapping
	public Page<PrestadorResponse> listar(@PageableDefault(size = 5) @SortDefault(sort = "nome") Pageable pageable) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());

		if (roles.contains("ROLE_ADMIN") || roles.contains("ROLE_CLIENTE")) {
			Page<Prestador> prestadorPage = prestadorRepository.findAll(pageable);
			return prestadorPage.map(prestador -> prestadorDtoAssembler.toModel(prestador));

		} else if (roles.contains("ROLE_PRESTADOR")) {
			String emailUser = authentication.getName();
			Prestador prestador = prestadorRepository.findByUsuarioEmail(emailUser)
					.orElseThrow(() -> new NegocioException("Prestador não encontrado."));
			return new PageImpl<>(Collections.singletonList(prestadorDtoAssembler.toModel(prestador)));
		}
		return new PageImpl<>(Collections.emptyList());
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
	@GetMapping("/regiao/{regiao}")
	public Page<PrestadorFiltroRegiaoResponse> listarPorRegiao(@PathVariable String regiao,
			@PageableDefault(size = 5) @SortDefault(sort = "nome") Pageable pageable) {

		Regiao regiaoEnum = Regiao.valueOf(regiao.toUpperCase());
		Page<Prestador> prestadorPage = catalogoPrestadorService.obterPrestadoresPorRegiao(regiaoEnum, pageable);
		return prestadorPage.map(prestador -> prestadorDtoAssembler.toModelMinFilter(prestador));
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
	@GetMapping("/nome-contem/{nome}")
	public Page<PrestadorFiltroRegiaoResponse> ListarPorNomeContem(@PathVariable String nome,
			@PageableDefault(size = 5) @SortDefault(sort = "nome") Pageable pageable) {
		Page<Prestador> prestadorPage = catalogoPrestadorService.buscarPorNomeContem(nome, pageable);
		return prestadorPage.map(prestador -> prestadorDtoAssembler.toModelMinFilter(prestador));
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE', 'PRESTADOR')")
	@GetMapping("/filtro")
	public Page<PrestadorFiltroRegiaoResponse> listarTodosNoFiltro(
			@PageableDefault(size = 1) @SortDefault(sort = "nome") Pageable pageable) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());

		if (roles.contains("ROLE_ADMIN") || roles.contains("ROLE_CLIENTE")) {
			Page<Prestador> prestadorPage = prestadorRepository.findAll(pageable);
			return prestadorPage.map(prestador -> prestadorDtoAssembler.toModelMinFilter(prestador));

		} else if (roles.contains("ROLE_PRESTADOR")) {
			String emailUser = authentication.getName();
			Prestador prestador = prestadorRepository.findByUsuarioEmail(emailUser)
					.orElseThrow(() -> new NegocioException("Prestador não encontrado."));
			return new PageImpl<>(Collections.singletonList(prestadorDtoAssembler.toModelMinFilter(prestador)));
		}
		return new PageImpl<>(Collections.emptyList());
	}

	@PreAuthorize("@acessoService.verificarAcessoProprioPrestador(#prestadorId) or hasAnyRole('ADMIN', 'CLIENTE')")
	@GetMapping("/perfis/{prestadorId}")
	public PrestadorMinResponse buscarPerfilPorId(@PathVariable Long prestadorId) {
		return prestadorDtoAssembler.toModelMin(catalogoPrestadorService.buscarOuFalhar(prestadorId));
	}
	
	@PreAuthorize("@acessoService.verificarAcessoProprioPrestador(#prestadorId) or hasAnyRole('ADMIN', 'CLIENTE')")
	@GetMapping("/perfil/{prestadorId}")
	public PrestadorMinResponse buscarPorIdPerfil(@PathVariable Long prestadorId) {
		return prestadorDtoAssembler.toModelMin(catalogoPrestadorService.buscarOuFalhar(prestadorId));
	}

//	//Remover caso não seja necessário... NÃO PAGINEI
//	@PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")	  
//	@GetMapping ("/perfis")
//	public List<PrestadorMinResponse> listarPerfilPrestador(){
//		return prestadorRepository.findAll()
//				.stream()
//				.map(prestador -> prestadorDtoAssembler.toModelMin(prestador))
//				.collect(Collectors.toList());
//	}

	@PreAuthorize("hasAnyRole('ADMIN', 'PRESTADOR')")
	@GetMapping("/{prestadorId}/ordens-servico")
	public List<OrdemServicoDataResponse> buscarPorDataServico(@PathVariable Long prestadorId,
			@RequestParam("data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataServico) {
		return ordemServicoDtoAssembler.toCollectionOSDataModel(
				catalogoPrestadorService.buscarOrdensServicoPorDataServico(prestadorId, dataServico));
	}

	@PreAuthorize("@acessoService.verificarAcessoProprioPrestador(#prestadorId) or hasAnyRole('ADMIN', 'CLIENTE')")
	@GetMapping("/{prestadorId}")
	public PrestadorResponse buscarPorId(@PathVariable Long prestadorId) {
		return prestadorDtoAssembler.toModel(catalogoPrestadorService.buscarOuFalhar(prestadorId));
	}

	
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

	@PreAuthorize("@acessoService.verificarAcessoProprioPrestador(#prestadorId) or hasRole('ADMIN')")
	@PutMapping("/{prestadorId}")
	public PrestadorResponse editar(@PathVariable Long prestadorId, @RequestBody @Valid PrestadorInput prestadorInput) {

		Prestador prestador = prestadorInputDisassembler.toDomainObject(prestadorInput);
		Prestador prestadorAtual = catalogoPrestadorService.buscarOuFalhar(prestadorId);

		BeanUtils.copyProperties(prestador, prestadorAtual, "id", "dataDoCadastro", "dataDaAtualizacao", "usuario");

		String nomeAtual = prestadorAtual.getNome();
		prestadorAtual.getUsuario().setNome(nomeAtual);

		return prestadorDtoAssembler.toModel(catalogoPrestadorService.salvar(prestadorAtual));

	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{prestadorId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long prestadorId) {
		Prestador prestadorAtual = catalogoPrestadorService.buscarOuFalhar(prestadorId);
		Long usuarioId = prestadorAtual.getUsuario().getId();
		catalogoPrestadorService.excluir(prestadorId);
		cadastroUsuarioService.excluir(usuarioId);

	}

}

package com.empreget.api.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.empreget.api.assembler.UsuarioDtoAssembler;
import com.empreget.api.assembler.UsuarioInputDisassembler;
import com.empreget.api.dto.UsuarioResponse;
import com.empreget.api.dto.input.SenhaInput;
import com.empreget.api.dto.input.UsuarioEmailInput;
import com.empreget.api.openApi.controller.UsuarioControllerOpenApi;
import com.empreget.domain.exception.NegocioException;
import com.empreget.domain.model.Usuario;
import com.empreget.domain.repository.UsuarioRepository;
import com.empreget.domain.service.CadastroUsuarioService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/usuarios", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsuarioController implements UsuarioControllerOpenApi{

	// private static final Logger log =
	// LoggerFactory.getLogger(UsuarioController.class);

	private UsuarioRepository usuarioRepository;
	private CadastroUsuarioService cadastroUsuarioService;
	private UsuarioInputDisassembler usuarioInputDisassembler;
	private UsuarioDtoAssembler usuarioDtoAssembler;

	@PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE', 'PRESTADOR')")
	@GetMapping
	public Page<UsuarioResponse> listar(@PageableDefault(size = 10) @SortDefault(sort = "id") Pageable pageable) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());

		if (roles.contains("ROLE_ADMIN")) {
			Page<Usuario> todosUsuariosPage = usuarioRepository.findAll(pageable);
			return todosUsuariosPage.map(usuario -> usuarioDtoAssembler.toModel(usuario));

		} else if (roles.contains("ROLE_CLIENTE") || roles.contains("ROLE_PRESTADOR")) {
			String username = authentication.getName();
			Usuario usuario = usuarioRepository.findUserByEmail(username)
					.orElseThrow(() -> new NegocioException("Usuário não encontrado."));
			return new PageImpl<>(Collections.singletonList(usuarioDtoAssembler.toModel(usuario)));
		}

		return new PageImpl<>(Collections.emptyList());
	}

	@PreAuthorize("@acessoService.verificarAcessoProprioUsuario(#usuarioId) or hasRole('ADMIN')")
	@GetMapping("/{usuarioId}")
	public UsuarioResponse buscar(@PathVariable Long usuarioId) {
		Usuario usuario = cadastroUsuarioService.buscarOuFalhar(usuarioId);

		return usuarioDtoAssembler.toModel(usuario);
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/email")
	public UsuarioResponse buscarUsuarioPorEmail(@RequestParam("email") String email) {
		Optional<Usuario> usuario = usuarioRepository.findUserByEmail(email);

		if (usuario.isPresent()) {
			return usuarioDtoAssembler.toModel(usuario.get());
		} else {
			throw new NegocioException(String.format("O e-mail '%s' não está cadastrado no sistema.", email));
		}
	}

	@PreAuthorize("@acessoService.verificarAcessoProprioUsuario(#usuarioId) and isAuthenticated()")
	@PutMapping("/{usuarioId}")
	public UsuarioResponse atualizarEmail(@PathVariable Long usuarioId,
			@RequestBody @Valid UsuarioEmailInput usuarioInput) {

		Usuario usuarioAtual = cadastroUsuarioService.buscarOuFalhar(usuarioId);
		usuarioInputDisassembler.copyToDomainObjectMail(usuarioInput, usuarioAtual);

		boolean emailEmUso = usuarioRepository.findByEmail(usuarioAtual.getEmail()).stream()
				.anyMatch(usuarioExistente -> !usuarioExistente.equals(usuarioAtual));

		if (emailEmUso) {
			throw new NegocioException(
					String.format("O email %d está em uso por outro usuário. Tente novamente com outro e-mail.",
							usuarioAtual.getEmail()));
		}

		return usuarioDtoAssembler.toModel(cadastroUsuarioService.salvarEdicao(usuarioAtual));
	}

	@PreAuthorize("@acessoService.verificarAcessoProprioUsuario(#usuarioId) or hasRole('ADMIN')")
	@PutMapping("/{usuarioId}/senha")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void alterarSenha(@PathVariable Long usuarioId,
			@RequestBody @Valid SenhaInput senhaInput) {
		cadastroUsuarioService.alterarSenha(usuarioId, senhaInput.getSenhaAtual(), senhaInput.getNovaSenha());
	}

}

package com.empreget.api.controller;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.empreget.api.assembler.ClienteDtoAssembler;
import com.empreget.api.assembler.ClienteInputDisassembler;
import com.empreget.api.dto.ClienteResponse;
import com.empreget.api.dto.input.ClienteInput;
import com.empreget.api.openApi.controller.ClienteControllerOpenApi;
import com.empreget.domain.exception.NegocioException;
import com.empreget.domain.model.Cliente;
import com.empreget.domain.model.Usuario;
import com.empreget.domain.model.enums.UserRole;
import com.empreget.domain.repository.ClienteRepository;
import com.empreget.domain.service.CadastroUsuarioService;
import com.empreget.domain.service.CatalogoClienteService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/clientes", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClienteController implements ClienteControllerOpenApi {

	private ClienteRepository clienteRepository;
	private CatalogoClienteService catalogoClienteService;
	private ClienteDtoAssembler clienteAssembler;
	private ClienteInputDisassembler clienteInputDisassembler;
	private CadastroUsuarioService cadastroUsuarioService;

	@PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
	@GetMapping
	public List<ClienteResponse> listar() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		List<String> roles = authentication.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());
		
		if(roles.contains("ROLE_ADMIN")) {
			return clienteRepository.findAll()
					.stream()
					.map(cliente -> clienteAssembler.toModel(cliente))
					.collect(Collectors.toList());
		}else if (roles.contains("ROLE_CLIENTE")) {
	        String emailUser = authentication.getName();
	        Cliente cliente = clienteRepository.findByUsuarioEmail(emailUser)
	                .orElseThrow(() -> new NegocioException("Cliente não encontrado."));

	        return Collections.singletonList(clienteAssembler.toModel(cliente));
	    }

	    return Collections.emptyList();
	}
	
	@PreAuthorize("@acessoService.verificarAcessoProprioCliente(#clienteId) or hasRole('ADMIN')")
	@GetMapping("/{clienteId}")
	public ClienteResponse buscarPorId(@PathVariable Long clienteId) {
	    return clienteAssembler.toModel(catalogoClienteService.buscarOuFalhar(clienteId));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ClienteResponse adicionar(@Valid @RequestBody ClienteInput clienteInput) {
		Cliente cliente = clienteInputDisassembler.toDomainObject(clienteInput);
		
		Usuario usuario = new Usuario();
		usuario.setEmail(cliente.getUsuario().getEmail());
		usuario.setSenha(cliente.getUsuario().getSenha());
		cliente.getUsuario().getRole();
		usuario.setRole(UserRole.CLIENTE);
		usuario.setNome(cliente.getNome());
		
		Usuario usuarioSalvo = cadastroUsuarioService.cadastrarUser(usuario);
		
        cliente.setUsuario(usuarioSalvo);
		return clienteAssembler.toModel(catalogoClienteService.salvar(cliente));
	}
	
	@PreAuthorize("@acessoService.verificarAcessoProprioCliente(#clienteId) or hasRole('ADMIN')")
	@PutMapping("/{clienteId}")
	public ClienteResponse editar(@PathVariable Long clienteId, @Valid @RequestBody ClienteInput clienteinput) {
		Cliente cliente = clienteInputDisassembler.toDomainObject(clienteinput);
		Cliente clienteAtual = catalogoClienteService.buscarOuFalhar(clienteId);

		BeanUtils.copyProperties(cliente, clienteAtual, 
				"id", "dataDoCadastro", "dataDaAtualizacao", "usuario");
		
		String nomeAtual = clienteAtual.getNome();
		clienteAtual.getUsuario().setNome(nomeAtual);

		return clienteAssembler.toModel(catalogoClienteService.salvar(clienteAtual));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{clienteId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void excluir (@PathVariable Long clienteId) {
		catalogoClienteService.excluir(clienteId);			
	}
}

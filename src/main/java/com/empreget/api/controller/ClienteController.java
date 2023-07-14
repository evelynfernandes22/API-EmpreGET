package com.empreget.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
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
import com.empreget.domain.model.Cliente;
import com.empreget.domain.model.Usuario;
import com.empreget.domain.model.enums.UserRole;
import com.empreget.domain.repository.ClienteRepository;
import com.empreget.domain.service.CadastroUsuarioService;
import com.empreget.domain.service.CatalogoClienteService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/clientes")
public class ClienteController {

	private ClienteRepository clienteRepository;
	private CatalogoClienteService catalogoClienteService;
	private ClienteDtoAssembler clienteAssembler;
	private ClienteInputDisassembler clienteInputDisassembler;
	private CadastroUsuarioService cadastroUsuarioService;


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

	@PutMapping("/{clienteId}")
	public ClienteResponse editar(@PathVariable Long clienteId, @Valid @RequestBody ClienteInput clienteinput) {
		Cliente cliente = clienteInputDisassembler.toDomainObject(clienteinput);

		Cliente clienteAtual = catalogoClienteService.buscarOuFalhar(clienteId);

		BeanUtils.copyProperties(cliente, clienteAtual, 
				"id", "dataDoCadastro", "dataDaAtualizacao", "usuario");

		return clienteAssembler.toModel(catalogoClienteService.salvar(clienteAtual));

	}

	@DeleteMapping("/{clienteId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long clienteId) {
		catalogoClienteService.excluir(clienteId);			
	}
}

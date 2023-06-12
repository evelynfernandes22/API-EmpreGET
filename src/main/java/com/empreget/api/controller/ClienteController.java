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
		
		//setar o usuario informado no clienteInput e salvar 
		Usuario usuario = new Usuario();
		usuario.setEmail(cliente.getUsuario().getEmail());
		usuario.setSenha(cliente.getUsuario().getSenha());
		usuario.setSouCliente(cliente.getUsuario().isSouCliente());
		usuario.setNome(cliente.getNome());
		
		//salvar usuário no banco antes do cliente
		Usuario usuarioSalvo = cadastroUsuarioService.salvar(usuario);
		
		// definir a referência para o usuário 
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

//COM DTO NÃO FAZ SENTIDO TER EDITAR PARCIAL
//	@PatchMapping("/{clienteId}")
//	public ClienteResponse editarParcial (@PathVariable Long clienteId, @Valid @RequestBody Map<String, Object> dados, 
//			HttpServletRequest request) {
//		
//		Cliente clienteAtual = catalogoClienteService.buscarOuFalhar(clienteId);
//		
//		merge(dados, clienteAtual, request);
//		
//		return clienteAssembler.toModel(catalogoClienteService.salvar(clienteAtual));
//	}
//	
	
//	private void merge(Map<String, Object> dadosOrigem, Cliente clienteDestino,
//			HttpServletRequest request) {
//		ServletServerHttpRequest serverHttpRequest = new ServletServerHttpRequest(request);
//		
//		try {
//			ObjectMapper objectMapper = new ObjectMapper();
//			objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
//			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
//			
//			Cliente clienteOrigem = objectMapper.convertValue(dadosOrigem, Cliente.class);
//			
//			dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
//				Field field = ReflectionUtils.findField(Cliente.class, nomePropriedade);
//				field.setAccessible(true);
//				
//				Object novoValor = ReflectionUtils.getField(field, clienteOrigem);
//				
//				ReflectionUtils.setField(field, clienteDestino, novoValor);
//			});
//		} catch (IllegalArgumentException e) {
//			Throwable rootCause = ExceptionUtils.getRootCause(e);
//			throw new HttpMessageNotReadableException(e.getMessage(), rootCause, serverHttpRequest);
//		}
//	}
	

	@DeleteMapping("/{clienteId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long clienteId) {
		catalogoClienteService.excluir(clienteId);			
	}
}

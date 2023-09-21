package com.empreget.api.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.empreget.api.assembler.OrdemServicoDtoAssembler;
import com.empreget.api.assembler.OrdemServicoInputDisassembler;
import com.empreget.api.dto.EnderecoResponse;
import com.empreget.api.dto.OrdemServicoResponse;
import com.empreget.api.dto.ServicoResponse;
import com.empreget.api.dto.input.OrdemServicoInput;
import com.empreget.api.openApi.controller.OSControllerOpenApi;
import com.empreget.domain.exception.ClienteNaoEncontradoException;
import com.empreget.domain.exception.NegocioException;
import com.empreget.domain.exception.OrdemServicoNaoEncontradoException;
import com.empreget.domain.model.Cliente;
import com.empreget.domain.model.OrdemServico;
import com.empreget.domain.repository.ClienteRepository;
import com.empreget.domain.repository.OrdemServicoRepository;
import com.empreget.domain.service.BuscaOSService;
import com.empreget.domain.service.CancelamentoOSService;
import com.empreget.domain.service.SolicitacaoOSService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/os", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrdemServicoController implements OSControllerOpenApi{

	private OrdemServicoRepository ordemServicoRepository;
	private SolicitacaoOSService solicitacaoOSService;
	private OrdemServicoDtoAssembler ordemServicoAssembler;
	private OrdemServicoInputDisassembler ordemServicoInputDisassembler;
	private CancelamentoOSService cancelamentoOSService;
	private ClienteRepository clienteRepository;
	private BuscaOSService buscaOSService;


	@PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public OrdemServicoResponse solicitar(@Valid @RequestBody OrdemServicoInput ordemServicoInput) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String emailCliente = authentication.getName();
		
		Cliente cliente = clienteRepository.findByUsuarioEmail(emailCliente)
				.orElseThrow(() -> new ClienteNaoEncontradoException("Cliente não encontrado registrado com o email " + emailCliente));
		
		OrdemServico ordemServico = ordemServicoInputDisassembler.toDomainObject(ordemServicoInput);
		ordemServico.setCliente(cliente);
		
		OrdemServicoResponse ordemServicoResponse = ordemServicoAssembler.toModel(solicitacaoOSService.solicitar(ordemServico));
		puxarEnderecoEServico(ordemServico, ordemServicoResponse);

		return ordemServicoResponse;
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE', 'PRESTADOR')")
	@GetMapping
	public Page<OrdemServicoResponse> listar(@PageableDefault(size = 5) @SortDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    List<String> roles = authentication.getAuthorities()
	            .stream()
	            .map(GrantedAuthority::getAuthority)
	            .collect(Collectors.toList());

	    String emailUser = authentication.getName();

	    if (roles.contains("ROLE_ADMIN")) {
	    	Page<OrdemServico> OSPage = ordemServicoRepository.findAll(pageable);
	    	return OSPage.map(ordemServico -> {
                OrdemServicoResponse ordemServicoResponse = ordemServicoAssembler.toModel(ordemServico);
                puxarEnderecoEServico(ordemServico, ordemServicoResponse);
                return ordemServicoResponse;
            });
	                
	    } else if (roles.contains("ROLE_CLIENTE")) {
	        Page<OrdemServico> ordemServicoListPage = ordemServicoRepository.findByClienteUsuarioEmail(emailUser, pageable);
	        if (!ordemServicoListPage.isEmpty()) {
	            return ordemServicoListPage.map(ordemServicoAssembler::toModel);
	        } else {
	            throw new NegocioException("Nenhuma Ordem de Serviço encontrada vinculada a este cliente.");
	        }
	    } else if (roles.contains("ROLE_PRESTADOR")) {
	        Page<OrdemServico> ordemServicoListPage = ordemServicoRepository.findByPrestadorUsuarioEmail(emailUser, pageable);
	        if (!ordemServicoListPage.isEmpty()) {
	            return ordemServicoListPage.map(ordemServicoAssembler::toModel);
	        } else {
	            throw new NegocioException("Nenhuma Ordem de Serviço encontrada vinculada a este prestador.");
	        }
	    }

	    return new PageImpl<>(Collections.emptyList());
	}

	@PreAuthorize("@acessoService.verificarAcessoProprioOrdemServico(#id) or hasAnyRole('ADMIN')")
	@GetMapping("/{id}")
	public OrdemServicoResponse buscar(@PathVariable Long id) {
		
		OrdemServico ordemServico = buscaOSService.buscarOuFalhar(id);
	    OrdemServicoResponse ordemServicoResponse = ordemServicoAssembler.toModel(ordemServico);
	    puxarEnderecoEServico(ordemServico, ordemServicoResponse);
	    return ordemServicoResponse;
	}	
	
	@PreAuthorize("@acessoService.verificarAcessoProprioOrdemServico(#ordemServicoId) and hasAnyRole('ADMIN', 'CLIENTE')")
	@PutMapping("/{ordemServicoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void cancelar (@PathVariable Long ordemServicoId){	
		cancelamentoOSService.cancelar(ordemServicoId);
		
	}
	
	@PreAuthorize("@acessoService.verificarAcessoProprioOrdemServico(#id) and hasRole('PRESTADOR')")
	@PutMapping("/{id}/aceite")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void aceitar (@PathVariable Long id) {
		solicitacaoOSService.aceitar(id);
	}
	
	@PreAuthorize("@acessoService.verificarAcessoProprioOrdemServico(#id) and hasRole('PRESTADOR')")
	@PutMapping("/{id}/recusa")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void recusar (@PathVariable Long id) {
		solicitacaoOSService.recusar(id);
	}
	
	@PreAuthorize("hasRole('PRESTADOR')")
	@PutMapping("/{id}/finalizacao")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void finalizar (@PathVariable Long id) {
		solicitacaoOSService.finalizar(id);
	}

	protected void puxarEnderecoEServico(OrdemServico ordemServico, OrdemServicoResponse ordemServicoResponse) {
		ordemServicoResponse.setLocalDoServico(new EnderecoResponse());
		ordemServicoResponse.getLocalDoServico().setLogradouro(ordemServico.getCliente().getEndereco().getLogradouro());
		ordemServicoResponse.getLocalDoServico().setNumero(ordemServico.getCliente().getEndereco().getNumero());
		ordemServicoResponse.getLocalDoServico().setComplemento(ordemServico.getCliente().getEndereco().getComplemento());
		ordemServicoResponse.getLocalDoServico().setBairro(ordemServico.getCliente().getEndereco().getBairro());
		ordemServicoResponse.getLocalDoServico().setCidade(ordemServico.getCliente().getEndereco().getCidade());
		ordemServicoResponse.getLocalDoServico().setEstado(ordemServico.getCliente().getEndereco().getEstado());
		ordemServicoResponse.getLocalDoServico().setPais(ordemServico.getCliente().getEndereco().getPais());
		
		ordemServicoResponse.setServico(new ServicoResponse());
		ordemServicoResponse.getServico().setDescricao(ordemServico.getPrestador().getServico().getDescricao());
		ordemServicoResponse.getServico().setValor(ordemServico.getPrestador().getServico().getValor());
	}
}

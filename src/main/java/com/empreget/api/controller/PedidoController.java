package com.empreget.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.empreget.api.assembler.PedidoAssembler;
import com.empreget.api.assembler.PedidoInputDisassembler;
import com.empreget.api.dto.EnderecoResponse;
import com.empreget.api.dto.PedidoResponse;
import com.empreget.api.dto.ServicoResponse;
import com.empreget.api.dto.input.PedidoInput;
import com.empreget.domain.model.Pedido;
import com.empreget.domain.repository.PedidoRepositoy;
import com.empreget.domain.service.FinalizacaoPedidoService;
import com.empreget.domain.service.SolicitacaoPedidoService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/pedidos")
public class PedidoController {

	private PedidoRepositoy pedidoRepository;
	private SolicitacaoPedidoService solicitacaoPedidoService;
	private PedidoAssembler pedidoAssembler;
	private PedidoInputDisassembler pedidoInputDisassembler;
	private FinalizacaoPedidoService finalizacaoPedidoService;


	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PedidoResponse solicitar(@Valid @RequestBody PedidoInput pedidoInput) {
		Pedido pedido = pedidoInputDisassembler.toDomainObject(pedidoInput);
		
		PedidoResponse pedidoResponse = pedidoAssembler.toModel(solicitacaoPedidoService.solicitar(pedido));
		puxarEnderecoEServico(pedido, pedidoResponse);

		return pedidoResponse;
	}


	@GetMapping
	public List<PedidoResponse> listar() {
		return pedidoRepository.findAll()
				.stream()
				.map(pedido -> {
					PedidoResponse pedidoResponse = pedidoAssembler.toModel(pedido);
					puxarEnderecoEServico(pedido, pedidoResponse);

					return pedidoResponse;
		}).collect(Collectors.toList());
	}


	@GetMapping("/{pedidoId}")
	public ResponseEntity<PedidoResponse> buscar(@PathVariable Long pedidoId) {
		return pedidoRepository.findById(pedidoId)
				.map(pedido -> {
					PedidoResponse pedidoResponse = pedidoAssembler.toModel(pedido);
					puxarEnderecoEServico(pedido, pedidoResponse);

					return ResponseEntity.ok(pedidoResponse);
				}).orElse(ResponseEntity.notFound().build());
	}
	
	@PutMapping("/{pedidoId}/aceite")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void aceitar (@PathVariable Long pedidoId) {
		solicitacaoPedidoService.aceitar(pedidoId);
	}
	
	@PutMapping("/{pedidoId}/recusa")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void recusar (@PathVariable Long pedidoId) {
		solicitacaoPedidoService.recusar(pedidoId);
	}
	
	@PutMapping("/{pedidoId}/finalizacao")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void finalizar (@PathVariable Long pedidoId) {
		finalizacaoPedidoService.finalizar(pedidoId);
	}

	protected void puxarEnderecoEServico(Pedido pedido, PedidoResponse pedidoResponse) {
		pedidoResponse.setLocalDoServico(new EnderecoResponse());
		pedidoResponse.getLocalDoServico().setLogradouro(pedido.getCliente().getEndereco().getLogradouro());
		pedidoResponse.getLocalDoServico().setNumero(pedido.getCliente().getEndereco().getNumero());
		pedidoResponse.getLocalDoServico().setComplemento(pedido.getCliente().getEndereco().getComplemento());
		pedidoResponse.getLocalDoServico().setBairro(pedido.getCliente().getEndereco().getBairro());
		pedidoResponse.getLocalDoServico().setCidade(pedido.getCliente().getEndereco().getCidade());
		pedidoResponse.getLocalDoServico().setEstado(pedido.getCliente().getEndereco().getEstado());
		pedidoResponse.getLocalDoServico().setPais(pedido.getCliente().getEndereco().getPais());
		
		pedidoResponse.setServico(new ServicoResponse());
		pedidoResponse.getServico().setDescricao(pedido.getPrestador().getServico().getDescricao());
		pedidoResponse.getServico().setValor(pedido.getPrestador().getServico().getValor());
	}
}

package com.empreget.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.empreget.api.dto.input.PedidoInput;
import com.empreget.domain.model.Pedido;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class PedidoInputDisassembler {

	private ModelMapper modelMapper;
	
	public Pedido toDomainObject (PedidoInput pedidoInput) {
		return modelMapper.map(pedidoInput, Pedido.class);
	}
}

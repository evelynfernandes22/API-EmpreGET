package com.empreget.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.empreget.api.dto.input.ClienteInput;
import com.empreget.domain.model.Cliente;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class ClienteInputDisassembler {

	private ModelMapper modelMapper;
	
	public Cliente toDomainObject (ClienteInput clienteInput) {
		return modelMapper.map(clienteInput, Cliente.class);
	}
	
	public void copyToDomainObject(ClienteInput clienteInput, Cliente cliente) {
		
		modelMapper.map(clienteInput, cliente);
	}
}

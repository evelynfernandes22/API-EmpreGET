package com.empreget.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.empreget.api.dto.input.PrestadorInput;
import com.empreget.domain.model.Prestador;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class PrestadorInputDisassembler {
	
	private ModelMapper modelMapper;
	
	public Prestador toDomainObject(PrestadorInput prestadorInput) {
		return modelMapper.map(prestadorInput, Prestador.class);
	}

	public void copyToDomainObjet(PrestadorInput prestadorInput, Prestador prestador) {
		modelMapper.map(prestadorInput, prestador);
	}
}

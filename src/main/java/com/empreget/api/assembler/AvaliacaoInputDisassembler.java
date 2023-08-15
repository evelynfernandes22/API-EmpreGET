package com.empreget.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.empreget.api.dto.input.AvaliacaoInput;
import com.empreget.domain.model.Avaliacao;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class AvaliacaoInputDisassembler {
	
	private ModelMapper modelMapper;
	
	public Avaliacao toDomainObject(AvaliacaoInput avaliacaoInput){
		return modelMapper.map(avaliacaoInput, Avaliacao.class);
	}
	
	public void copyToDomainObject(AvaliacaoInput avaliacaoInput, Avaliacao avaliacao) {
		modelMapper.map(avaliacaoInput, avaliacao);
	}
		
}

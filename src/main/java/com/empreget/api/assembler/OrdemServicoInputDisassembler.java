package com.empreget.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.empreget.api.dto.input.OrdemServicoInput;
import com.empreget.domain.model.OrdemServico;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class OrdemServicoInputDisassembler {

	private ModelMapper modelMapper;
	
	public OrdemServico toDomainObject (OrdemServicoInput ordemServicoInput) {
		return modelMapper.map(ordemServicoInput, OrdemServico.class);
	}
	
	public void copyToDomainObject(OrdemServicoInput ordemServicoInput, OrdemServico ordemServico) {
		modelMapper.map(ordemServicoInput, ordemServico);
	}
}

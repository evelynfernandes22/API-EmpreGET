package com.empreget.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.empreget.api.dto.OrdemServicoResponse;
import com.empreget.domain.model.OrdemServico;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class OrdemServicoDtoAssembler {

	private ModelMapper modelMapper;
	
	
	public OrdemServicoResponse toModel (OrdemServico ordemServico) {
		return modelMapper.map(ordemServico, OrdemServicoResponse.class);
	}
	
	
	public List<OrdemServicoResponse> toCollectionModel(List<OrdemServico> ordemServicos){
		return ordemServicos.stream()
				.map(this::toModel)
				.collect(Collectors.toList());
	}
		
}

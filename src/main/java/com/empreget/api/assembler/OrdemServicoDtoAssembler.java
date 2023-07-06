package com.empreget.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.empreget.api.dto.OrdemServicoDataResponse;
import com.empreget.api.dto.OrdemServicoResponse;
import com.empreget.domain.model.OrdemServico;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class OrdemServicoDtoAssembler {

	private ModelMapper modelMapper;
	
	
	public OrdemServicoResponse toModel (OrdemServico ordensServico) {
		return modelMapper.map(ordensServico, OrdemServicoResponse.class);
	}
	public OrdemServicoDataResponse toOSDataModel (OrdemServico ordensServico) {
		return modelMapper.map(ordensServico, OrdemServicoDataResponse.class);
	}
	
	
	public List<OrdemServicoResponse> toCollectionModel(List<OrdemServico> ordensServico){
		return ordensServico.stream()
				.map(this::toModel)
				.collect(Collectors.toList());
	}
	public List<OrdemServicoDataResponse> toCollectionOSDataModel(List<OrdemServico> ordensServico){
		return ordensServico.stream()
				.map(this::toOSDataModel)
				.collect(Collectors.toList());
	}
		
}

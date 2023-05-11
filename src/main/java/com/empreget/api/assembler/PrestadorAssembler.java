package com.empreget.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.empreget.api.dto.PrestadorResponse;
import com.empreget.domain.model.Prestador;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class PrestadorAssembler {
	
	private ModelMapper modelMapper;
	
	public PrestadorResponse toModel (Prestador prestador) {
		return modelMapper.map(prestador, PrestadorResponse.class);
	}
	
	
	public List<PrestadorResponse> toCollectionModel (List<Prestador> prestador){
		return prestador.stream()
				.map(this::toModel)
				.collect(Collectors.toList());
	}

}

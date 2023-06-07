package com.empreget.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.empreget.api.dto.PrestadorFiltroRegiaoResponse;
import com.empreget.api.dto.PrestadorMinResponse;
import com.empreget.api.dto.PrestadorResponse;
import com.empreget.domain.model.Prestador;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class PrestadorDtoAssembler {
	
	private ModelMapper modelMapper;
	
	public PrestadorResponse toModel (Prestador prestador) {
		return modelMapper.map(prestador, PrestadorResponse.class);
	}
	
	public PrestadorMinResponse toModelMin (Prestador prestador) {
		return modelMapper.map(prestador, PrestadorMinResponse.class);
	}

	public List<PrestadorResponse> toCollectionModel (List<Prestador> prestador){
		return prestador.stream()
				.map(this::toModel)
				.collect(Collectors.toList());
	}
	
	
	
	//FILTRO DE REGIAO ou FINDBYNAMECONTAIN
	
	public PrestadorFiltroRegiaoResponse toModelMinFilter (Prestador prestador) {
		return modelMapper.map(prestador, PrestadorFiltroRegiaoResponse.class);
	}
	
	
	public List<PrestadorFiltroRegiaoResponse> toCollectionMinFilterModel (List<Prestador> prestador){
		return prestador.stream()
				.map(this::toModelMinFilter)
				.collect(Collectors.toList());
	}

}

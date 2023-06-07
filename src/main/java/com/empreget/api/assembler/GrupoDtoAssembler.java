package com.empreget.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.empreget.api.dto.GrupoResponse;
import com.empreget.domain.model.Grupo;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class GrupoDtoAssembler {

private ModelMapper modelMapper;
	
	public GrupoResponse toModel(Grupo grupo) {
		return modelMapper.map(grupo, GrupoResponse.class);
	}
	
	public List<GrupoResponse> toCollectionModel(List<Grupo> grupos){
		return grupos.stream()
				.map(grupo -> toModel(grupo))
				.collect(Collectors.toList());
	}
}

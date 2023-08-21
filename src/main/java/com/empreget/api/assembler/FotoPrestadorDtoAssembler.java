package com.empreget.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.empreget.api.dto.FotoPrestadorResponse;
import com.empreget.domain.model.FotoPrestador;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class FotoPrestadorDtoAssembler {
	
	private ModelMapper modelMapper;
	
	public FotoPrestadorResponse toModel (FotoPrestador foto) {
		return modelMapper.map(foto, FotoPrestadorResponse.class);
	}
	

}

package com.empreget.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.empreget.api.dto.input.GrupoInput;
import com.empreget.domain.model.Grupo;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class GrupoInputDisassembler {

private ModelMapper modelMapper;
	
	public Grupo toDomainModel(GrupoInput grupoInput) {
		return modelMapper.map(grupoInput, Grupo.class);
	}
	
	public void copyToDomainObject(GrupoInput grupoInput, Grupo grupo) {
		modelMapper.map(grupoInput, grupo);
	}
}

package com.empreget.api.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.empreget.api.dto.PermissaoResponse;
import com.empreget.domain.model.Permissao;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class PermissaoDtoAssembler {

	private ModelMapper modelMapper;
	
	public PermissaoResponse toModel(Permissao permissao) {
		return modelMapper.map(permissao, PermissaoResponse.class);
	}
	
	public List<PermissaoResponse> toCollectionModel(Collection<Permissao> permissoes){
		return permissoes.stream()
				.map(permissao -> toModel(permissao))
				.collect(Collectors.toList());
	}
}

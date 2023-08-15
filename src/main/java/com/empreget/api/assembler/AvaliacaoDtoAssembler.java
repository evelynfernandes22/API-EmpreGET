package com.empreget.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.empreget.api.dto.AvaliacaoResponse;
import com.empreget.domain.model.Avaliacao;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class AvaliacaoDtoAssembler {

	private ModelMapper modelMapper;
	
	public AvaliacaoResponse toModel(Avaliacao avaliacao) {
		return modelMapper.map(avaliacao, AvaliacaoResponse.class);
	}
	
	public List<AvaliacaoResponse> toCollectionModel(List<Avaliacao> avaliacoes){
		return avaliacoes.stream()
				.map(avaliacao -> toModel(avaliacao))
				.collect(Collectors.toList());
	}
}

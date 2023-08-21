package com.empreget.domain.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.empreget.domain.model.FotoPrestador;
import com.empreget.domain.repository.PrestadorRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CatalogoPrestadorFotoService {

	private PrestadorRepository prestadorRepository;
	
	@Transactional
	public FotoPrestador salvar(FotoPrestador foto) {
		
		Long prestadorId = foto.getPrestador().getId();
		Optional<FotoPrestador> fotoExistente = prestadorRepository.findFotoById(prestadorId);
		
		if(fotoExistente.isPresent()) {
			prestadorRepository.excluir(fotoExistente.get());
		}
		
		return prestadorRepository.save(foto);
	}
}

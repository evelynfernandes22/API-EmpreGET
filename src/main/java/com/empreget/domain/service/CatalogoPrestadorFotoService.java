package com.empreget.domain.service;

import java.io.InputStream;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.empreget.domain.exception.FotoNaoEncontradaException;
import com.empreget.domain.model.FotoPrestador;
import com.empreget.domain.repository.PrestadorRepository;
import com.empreget.domain.service.FotoStorageService.NovaFoto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CatalogoPrestadorFotoService {

	private PrestadorRepository prestadorRepository;
	private FotoStorageService fotoStorageService;
	
	@Transactional
	public FotoPrestador salvar(FotoPrestador foto, InputStream dadosArquivo) {
		
		Long prestadorId = foto.getPrestador().getId();
		String novoNomeArquivo = fotoStorageService.gerarNomeArquivo(foto.getNomeArquivo());
		String nomeArquivoExistente = null;
		
		Optional<FotoPrestador> fotoExistente = prestadorRepository.findFotoById(prestadorId);
		
		if(fotoExistente.isPresent()) {
			nomeArquivoExistente = fotoExistente.get().getNomeArquivo();
			prestadorRepository.excluir(fotoExistente.get());
		}
		
		foto.setNomeArquivo(novoNomeArquivo);
		foto = prestadorRepository.save(foto);
		prestadorRepository.flush();
		
		NovaFoto novaFoto = NovaFoto.builder()
				.nomeArquivo(foto.getNomeArquivo())
				.inputStream(dadosArquivo)
				.build();
		
		fotoStorageService.substituir(nomeArquivoExistente, novaFoto);
		
		return foto;
	}
	
	public FotoPrestador buscarOuFalhar(Long prestadorId) {
	    return prestadorRepository.findFotoById(prestadorId)
	            .orElseThrow(() -> new FotoNaoEncontradaException(prestadorId));
	}
	
	@Transactional
	public void remover(Long prestadorId) {
		
		FotoPrestador foto = buscarOuFalhar(prestadorId);
		prestadorRepository.excluir(foto);
		prestadorRepository.flush();
			
		fotoStorageService.remover(foto.getNomeArquivo());
	}
	
}

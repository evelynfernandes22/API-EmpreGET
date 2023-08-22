package com.empreget.infraestructure.service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.flywaydb.core.internal.util.FileCopyUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.empreget.domain.service.FotoStorageService;

@Service
public class LocalFotoStorageService implements FotoStorageService {

	@Value("${empreget.storage.local.diretorio-fotos}")
	private Path diretorioFotos; 
	
	@Override
	public void armazenar(NovaFoto novaFoto) {
		
		try {
			Path arquivoPath = getArquivoPath(novaFoto.getNomeArquivo());
			
			FileCopyUtils.copy(novaFoto.getInputStream(), Files.newOutputStream(arquivoPath));
		} catch (Exception e) {
			throw new StorageException("Não foi possível armazenar arquivo.", e);
		}
	}

	private Path getArquivoPath(String nomeArquivo) {
		return diretorioFotos.resolve(Path.of(nomeArquivo));
	}

	@Override
	public void remover(String nomeArquivo) {
		Path arquivoPath = getArquivoPath(nomeArquivo);
		
		try {
			Files.deleteIfExists(arquivoPath);
		} catch (Exception e) {
			throw new StorageException("Não foi possível excluir o arquivo.",e);
		}
	}

	@Override
	public InputStream recuperar(String nomeArquivo) {
		
		try {
			Path arquivoPath = getArquivoPath(nomeArquivo);
			return Files.newInputStream(arquivoPath);
			
		} catch (Exception e) {
			throw new StorageException("Não foi possíel recuperar o arquivo.", e);
		}
	}

}

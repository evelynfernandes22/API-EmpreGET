package com.empreget.api.controller;

import java.nio.file.Path;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/prestadores/{prestadorId}/foto")
public class PrestadorFotoController {

	@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public void atualizarFoto(@PathVariable Long prestadorId, @RequestParam MultipartFile arquivo) {
		
		var nomeArquivo = UUID.randomUUID().toString() + "_" + arquivo.getOriginalFilename();
		var arquivoFoto = Path.of("\\Users\\evely\\OneDrive\\Imagens\\foto_prestador", nomeArquivo);
		
		System.out.println(arquivoFoto);
		System.out.println(arquivo.getContentType());
		
		try {
			arquivo.transferTo(arquivoFoto);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}

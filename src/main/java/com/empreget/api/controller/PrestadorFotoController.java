package com.empreget.api.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.empreget.api.assembler.FotoPrestadorDtoAssembler;
import com.empreget.api.dto.FotoPrestadorResponse;
import com.empreget.api.dto.input.FotoPrestadorInput;
import com.empreget.domain.model.FotoPrestador;
import com.empreget.domain.model.Prestador;
import com.empreget.domain.service.CatalogoPrestadorFotoService;
import com.empreget.domain.service.CatalogoPrestadorService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/prestadores/{prestadorId}/foto")
public class PrestadorFotoController {

	private CatalogoPrestadorService catalogoPrestadorService;
	private CatalogoPrestadorFotoService catalogoPrestadorFotoService;
	private FotoPrestadorDtoAssembler fotoPrestadorDtoAssembler;
	
	@PreAuthorize("@acessoService.verificarAcessoProprioPrestador(#prestadorId)")
	@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public FotoPrestadorResponse atualizarFoto(@PathVariable Long prestadorId,
			@Valid FotoPrestadorInput fotoPrestadorInput) throws IOException {

		Prestador prestador = catalogoPrestadorService.buscarOuFalhar(prestadorId);
		MultipartFile arquivo = fotoPrestadorInput.getArquivo();
		
		
		FotoPrestador fotoPrestador = new FotoPrestador();
		fotoPrestador.setNomeArquivo(arquivo.getOriginalFilename());
		fotoPrestador.setContentType(arquivo.getContentType());
		fotoPrestador.setTamanho(arquivo.getSize());
		fotoPrestador.setPrestador(prestador);
	
		FotoPrestador fotoSalva = catalogoPrestadorFotoService.salvar(fotoPrestador,arquivo.getInputStream());
	
		return fotoPrestadorDtoAssembler.toModel(fotoSalva);	
	}
	
	@PreAuthorize("@acessoService.verificarAcessoProprioPrestador(#prestadorId) or hasAnyRole('ADMIN', 'CLIENTE')")
	@GetMapping
	public FotoPrestadorResponse buscar(@PathVariable Long prestadorId) {
		return fotoPrestadorDtoAssembler.toModel(catalogoPrestadorFotoService
				.buscarOuFalhar(prestadorId));
	}
}

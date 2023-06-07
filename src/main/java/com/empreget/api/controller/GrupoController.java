package com.empreget.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.empreget.api.assembler.GrupoDtoAssembler;
import com.empreget.api.assembler.GrupoInputDisassembler;
import com.empreget.api.dto.GrupoResponse;
import com.empreget.api.dto.input.GrupoInput;
import com.empreget.domain.exception.GrupoNaoEncontradoException;
import com.empreget.domain.exception.NegocioException;
import com.empreget.domain.model.Grupo;
import com.empreget.domain.repository.GrupoRepository;
import com.empreget.domain.service.CadastroGrupoService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/grupos")
public class GrupoController {

	private CadastroGrupoService cadastroGrupoService;
	private GrupoRepository grupoRepository;
	private GrupoDtoAssembler grupoDtoAssembler;
	private GrupoInputDisassembler grupoInputDisassembler;
	
	@GetMapping
	public List<GrupoResponse> listar(){
		return grupoDtoAssembler.toCollectionModel(grupoRepository.findAll());
	}
	
	@GetMapping("/{grupoId}")
	public GrupoResponse buscar(@PathVariable Long grupoId) {
		return grupoDtoAssembler.toModel(cadastroGrupoService.buscarOuFalhar(grupoId));
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public GrupoResponse adicionar(@RequestBody @Valid GrupoInput grupoInput) {
		
		Grupo grupo = grupoInputDisassembler.toDomainModel(grupoInput);
		return grupoDtoAssembler.toModel(cadastroGrupoService.salvar(grupo));
	}
	
	@PutMapping("/{grupoId}")
	public GrupoResponse editar(@RequestBody @Valid GrupoInput grupoInput, @PathVariable Long grupoId) {
		try {
			Grupo grupoAtual = cadastroGrupoService.buscarOuFalhar(grupoId);
			grupoInputDisassembler.copyToDomainObject(grupoInput, grupoAtual);
			return grupoDtoAssembler.toModel(cadastroGrupoService.salvar(grupoAtual));
			
		}catch (GrupoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}
	
	@DeleteMapping("/{grupoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long grupoId) {
		cadastroGrupoService.excluir(grupoId);
	}

}

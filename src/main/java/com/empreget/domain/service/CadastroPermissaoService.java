package com.empreget.domain.service;

import org.springframework.stereotype.Service;

import com.empreget.domain.exception.PermissaoNaoEncontradaException;
import com.empreget.domain.model.Permissao;
import com.empreget.domain.repository.PermissaoRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CadastroPermissaoService {

	private PermissaoRepository permissaoRepository;
	
	public Permissao buscarOuFalhar(Long permissaoId) {
		return permissaoRepository.findById(permissaoId)
				.orElseThrow(() -> new PermissaoNaoEncontradaException(permissaoId));
	}
}

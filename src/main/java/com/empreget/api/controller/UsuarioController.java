package com.empreget.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.empreget.api.assembler.UsuarioDtoAssembler;
import com.empreget.api.assembler.UsuarioInputDisassembler;
import com.empreget.api.dto.UsuarioResponse;
import com.empreget.api.dto.input.SenhaInput;
import com.empreget.api.dto.input.UsuarioComSenhaInput;
import com.empreget.api.dto.input.UsuarioEmailInput;
import com.empreget.domain.model.Usuario;
import com.empreget.domain.repository.UsuarioRepository;
import com.empreget.domain.service.CadastroUsuarioService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
	
	private UsuarioRepository usuarioRepository;
	private CadastroUsuarioService cadastroUsuarioService;
	private UsuarioInputDisassembler usuarioInputDisassembler;
	private UsuarioDtoAssembler usuarioDtoAssembler;
	

	@GetMapping
    public List<UsuarioResponse> listar() {
        List<Usuario> todosUsuarios = usuarioRepository.findAll();
        
        return usuarioDtoAssembler.toCollectionModel(todosUsuarios);
    }
    
    @GetMapping("/{usuarioId}")
    public UsuarioResponse buscar(@PathVariable Long usuarioId) {
        Usuario usuario = cadastroUsuarioService.buscarOuFalhar(usuarioId);
        
        return usuarioDtoAssembler.toModel(usuario);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse adicionar(@RequestBody @Valid UsuarioComSenhaInput usuarioComSenhaInput) {
        Usuario usuario = usuarioInputDisassembler.toDomainModel(usuarioComSenhaInput);
        
        return usuarioDtoAssembler.toModel(cadastroUsuarioService.salvar(usuario));
    }
    
    @PutMapping("/{usuarioId}")
    public UsuarioResponse atualizarEmail(@PathVariable Long usuarioId,
            @RequestBody @Valid UsuarioEmailInput usuarioInput) {
        Usuario usuarioAtual = cadastroUsuarioService.buscarOuFalhar(usuarioId);
        usuarioInputDisassembler.copyToDomainObjectMail(usuarioInput, usuarioAtual);
               
        return usuarioDtoAssembler.toModel(cadastroUsuarioService.salvar(usuarioAtual));
    }
    
    @PutMapping("/{usuarioId}/senha")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void alterarSenha(@PathVariable Long usuarioId, @RequestBody @Valid SenhaInput senhaInput) {
    	cadastroUsuarioService.alterarSenha(usuarioId, senhaInput.getSenhaAtual(), senhaInput.getNovaSenha());
    }  
    
}


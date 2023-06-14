package com.empreget.api.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.empreget.domain.model.Usuario;
import com.empreget.domain.service.CadastroUsuarioService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/usuarios")
public class AutenticacaoController {

	private CadastroUsuarioService cadastroUsuarioService;

/*
 *  Este GET é o que abre a tela de login
 *  usar a biblioteca HttpClient do Angular para fazer a requisição http 
 */
	@GetMapping("/login")
	public String exibirFormularioLogin(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "login";
	}

	
	/*
	 * No ionic é preciso fazer a lógica da rota para funcionar este POST
	 */
	@PostMapping("/login")
	public ResponseEntity<Object> autenticarUsuario(@ModelAttribute("usuario") Usuario usuario) {
	    Optional<Usuario> usuarioOptional = cadastroUsuarioService.autenticarUsuario(usuario.getEmail(),
	            usuario.getSenha());

	    if (usuarioOptional.isPresent()) {
	    	
	        String rotaRedirecionamento;
	        if (usuarioOptional.get().isSouCliente()) {
	            rotaRedirecionamento = "/cliente/home.page";
	        } else {
	            rotaRedirecionamento = "/tela-inicial-prestador.page";
	        }

	        Map<String, Object> response = new HashMap<>();
	        response.put("rotaRedirecionamento", rotaRedirecionamento);
	        response.put("mensagem", "Autenticação bem-sucedida");

	        return ResponseEntity.ok(response);
	    }

	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
	}

}

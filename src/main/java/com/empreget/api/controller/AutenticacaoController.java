package com.empreget.api.controller;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

	
	@PostMapping("/login")
	public String autenticarUsuario(@ModelAttribute("usuario") Usuario usuario, HttpSession session,
			RedirectAttributes redirectAttributes) {

		Optional<Usuario> usuarioOptional = cadastroUsuarioService.autenticarUsuario(usuario.getEmail(),
				usuario.getSenha());

		if (usuarioOptional.isPresent()) {
			
			//abre a sessão
			session.setAttribute("usuarioLogado", usuarioOptional.get());
			
			//direcionamento de ambiente
			if (usuarioOptional.get().isSouCliente()) {
				return "redirect:/home.page"; //redirect: rota do ionic, estou em dúvida se precisa do page
			} else {
				return "redirect:/tela-inicial-prestador.page"; 
			}
		}

		redirectAttributes.addFlashAttribute("mensagemErro", "Credenciais inválidas");
		return "redirect:/login";
	}


}

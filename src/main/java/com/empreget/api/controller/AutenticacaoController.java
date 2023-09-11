package com.empreget.api.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.empreget.api.dto.LoginResponse;
import com.empreget.api.dto.input.UsuarioEmailSenhaInput;
import com.empreget.core.config.security.TokenService;
import com.empreget.domain.model.Usuario;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Acesso")
@RestController
@RequestMapping("/auth")
public class AutenticacaoController {


	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private TokenService tokenService;
	
	@ApiOperation("Efetua login na aplicação")
	@PostMapping("/login")
	public ResponseEntity login(@RequestBody @Valid UsuarioEmailSenhaInput input) {
		try {
			var emailESenha  = new UsernamePasswordAuthenticationToken(input.getEmail(), input.getSenha());
			var auth = this.authenticationManager.authenticate(emailESenha);
						
			  var token = tokenService.geradorDeToken((Usuario) auth.getPrincipal());
			 			
			return ResponseEntity.ok(new LoginResponse(token));
		}catch (BadCredentialsException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body("Dados inválidos, tente novamente com credenciais registradas.");
		}
	}
	
}

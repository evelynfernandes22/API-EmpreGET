package com.empreget.domain.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.empreget.domain.model.Cliente;
import com.empreget.domain.model.Prestador;
import com.empreget.domain.model.Usuario;
import com.empreget.domain.repository.ClienteRepository;
import com.empreget.domain.repository.PrestadorRepository;

@Service
public class AuthService {

	// Classe de serviço para autenticação

	 @Autowired
	    private ClienteRepository clienteRepository;

	    @Autowired
	    private PrestadorRepository prestadorRepository;

//	    colocar usuário como pai de cliente e Prestador
	    
//	    public Usuario autenticar(String email, String senha, boolean souCliente) {
//	        Usuario usuario = null;
//
//	        if (souCliente) {
//	            Optional<Cliente> cliente = clienteRepository.findByEmail(email);
//	            if (cliente.isPresent() && cliente.get().get.getSenha().equals(senha)) {
//	                usuario = cliente.get();
//	            }
//	        } else {
//	            Prestador prestador = prestadorRepository.findByEmail(email);
//	            if (prestador != null && prestador.getSenha().equals(senha)) {
//	                usuario = prestador;
//	            }
//	        }
//
//	        return usuario;
//	    }
}

package com.empreget.core.config.security;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.empreget.domain.model.Cliente;
import com.empreget.domain.model.OrdemServico;
import com.empreget.domain.model.Prestador;
import com.empreget.domain.model.Usuario;
import com.empreget.domain.repository.OrdemServicoRepository;
import com.empreget.domain.service.CadastroUsuarioService;
import com.empreget.domain.service.CatalogoClienteService;
import com.empreget.domain.service.CatalogoPrestadorService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AcessoService {

	private CatalogoPrestadorService catalogoPrestadorService;
	private CatalogoClienteService catalogoClienteService;
	private CadastroUsuarioService cadastroUsuarioService;
	private OrdemServicoRepository ordemServicoRepository;

	public boolean verificarAcessoProprioPrestador(Long prestadorId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) authentication.getPrincipal();

		Prestador prestador = catalogoPrestadorService.buscarOuFalhar(prestadorId);

		if (prestador == null) {
			return false;
		}

		return prestador.getUsuario().getEmail().equals(usuarioAutenticado.getEmail());
	}

	public boolean verificarAcessoProprioCliente(Long clienteId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) authentication.getPrincipal();

		Cliente cliente = catalogoClienteService.buscarOuFalhar(clienteId);
		if (cliente == null) {
			return false;
		}
		return cliente.getUsuario().getEmail().equals(usuarioAutenticado.getEmail());
	}

	public boolean verificarAcessoProprioUsuario(Long usuarioId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) authentication.getPrincipal();

		Usuario usuario = cadastroUsuarioService.buscarOuFalhar(usuarioId);
		if (usuario == null) {
			return false;
		}

		return usuario.getEmail().equals(usuarioAutenticado.getEmail());
	}

	public boolean verificarAcessoProprioOrdemServico(Long osId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String emailUsuarioAutenticado = authentication.getName();

		Optional<OrdemServico> ordemServicoOptional = ordemServicoRepository.findById(osId);

		if (ordemServicoOptional.isPresent()) {
			OrdemServico ordemServico = ordemServicoOptional.get();

			return (ordemServico.getCliente().getUsuario().getEmail().equals(emailUsuarioAutenticado)
					|| (ordemServico.getPrestador() != null
							&& ordemServico.getPrestador().getUsuario().getEmail().equals(emailUsuarioAutenticado)));
		}

		return false;
	}
}

package com.empreget.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.empreget.domain.model.Prestador;

public interface PrestadorRepository extends JpaRepository<Prestador, Long> {
	
	Optional<Prestador> findByNome(String nome);

	List<Prestador> findByNomeContaining(String nome);
	
	boolean existsByNome (String nome);
	
	Optional<Prestador> findByEmail(String email);

}

package com.empreget.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.empreget.domain.model.Prestador;
import com.empreget.domain.model.enums.Regiao;

public interface PrestadorRepository extends JpaRepository<Prestador, Long> {
	
	Optional<Prestador> findByNome(String nome);

	List<Prestador> findByNomeContaining(String nome);
	
	boolean existsByNome (String nome);
	
	Optional<Prestador> findByEmail(String email);
	
	@Query("from Prestador p where p.regiao = :regiao")
	List<Prestador> findAllByRegiao(@Param("regiao") Regiao regiao);

}

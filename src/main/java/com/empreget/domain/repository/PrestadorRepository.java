package com.empreget.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.empreget.domain.model.FotoPrestador;
import com.empreget.domain.model.Prestador;
import com.empreget.domain.model.enums.Regiao;

@Repository
public interface PrestadorRepository extends JpaRepository<Prestador, Long>, PrestadorRepositoryQueries {
	
	Optional<Prestador> findByNome(String nome);

	Page<Prestador> findByNomeContaining(String nome, Pageable pageable);
	
	boolean existsByNome (String nome);
	
	Optional<Prestador> findByUsuarioEmail(String email);
	
	@Query("from Prestador p where p.regiao = :regiao")
	Page<Prestador> findAllByRegiao(@Param("regiao") Regiao regiao, Pageable pageable);
	
//	@Query("from FotoPrestador f where f.prestador.id= :prestadorId")
	@Query("select f from FotoPrestador f " +
	           "join fetch f.prestador p " +
	           "join fetch p.usuario u " +
	           "where p.id = :prestadorId")
	Optional<FotoPrestador> findFotoById(@Param("prestadorId") Long prestadorId);
}

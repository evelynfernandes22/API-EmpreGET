package com.empreget.domain.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.empreget.domain.model.Avaliacao;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
	
	Page<Avaliacao> findByPrestadorId(Long prestadorId, Pageable pageable);
	
	Page<Avaliacao> findByOrdemServicoId(Long ordemServicoId, Pageable pageable);
	
	Page<Avaliacao> findByPrestadorIdAndClienteId(Long prestadorId,Long clienteId, Pageable pageable);
	
	Page<Avaliacao> findByOrdemServicoIdAndClienteId(Long ordemServicoId,Long clienteId, Pageable pageable);
	
	List<Avaliacao> findByPrestadorId(Long prestadorId);
	
	Long countByPrestadorId(Long PrestadorId);
}

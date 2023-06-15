package com.empreget.domain.repository;

import java.time.OffsetDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.empreget.domain.model.OrdemServico;
import com.empreget.domain.model.Prestador;

@Repository
public interface OrdemServicoRepositoy extends JpaRepository<OrdemServico, Long>{

	boolean existsByPrestadorAndDataServico(Prestador prestador, OffsetDateTime dataServico);
	

}

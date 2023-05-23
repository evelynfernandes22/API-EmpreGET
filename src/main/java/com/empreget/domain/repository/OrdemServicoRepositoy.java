package com.empreget.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.empreget.domain.model.OrdemServico;

@Repository
public interface OrdemServicoRepositoy extends JpaRepository<OrdemServico, Long>{
	

}

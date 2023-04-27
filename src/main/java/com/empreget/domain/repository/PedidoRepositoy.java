package com.empreget.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.empreget.domain.model.Pedido;

@Repository
public interface PedidoRepositoy extends JpaRepository<Pedido, Long>{
	

}

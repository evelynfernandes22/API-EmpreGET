package com.empreget.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.empreget.domain.model.Cliente;
import com.empreget.domain.model.Prestador;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

	Optional<Prestador> findByEmail(String email);
}

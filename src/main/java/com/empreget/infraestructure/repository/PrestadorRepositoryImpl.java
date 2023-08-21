package com.empreget.infraestructure.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.empreget.domain.model.FotoPrestador;
import com.empreget.domain.repository.PrestadorRepositoryQueries;

@Repository
public class PrestadorRepositoryImpl implements PrestadorRepositoryQueries{

	@PersistenceContext
	private EntityManager manager;

	@Transactional
	@Override
	public FotoPrestador save(FotoPrestador foto) {
		return manager.merge(foto);
	}

	@Transactional
	@Override
	public void excluir(FotoPrestador foto) {
		manager.remove(foto);
	}
	
	
}

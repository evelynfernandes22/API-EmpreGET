package com.empreget.domain.repository;

import com.empreget.domain.model.FotoPrestador;


public interface PrestadorRepositoryQueries {

	FotoPrestador save (FotoPrestador foto);
	void excluir(FotoPrestador foto);
}

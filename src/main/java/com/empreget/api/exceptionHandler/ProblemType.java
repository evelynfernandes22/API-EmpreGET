package com.empreget.api.exceptionHandler;

import lombok.Getter;

@Getter
public enum ProblemType {

	DADOS_INVALIDOS ("/dados-invalidos", "Dados inválidos"),
	ERRO_DE_SISTEMA("/erro-de-sistema", "Erro de sistema"),
	PARAMETRO_INVALIDO("/parametro-invalido", "Parâmetro inválido"),
	MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensivel", "Mensagem incompreensível"),
	RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado", "Recurso não encontrado"),
	ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso"),
	ACESSO_NEGADO("/acesso-negado", "Acesso negado"),
	ERRO_NEGOCIO("/erro-negocio", "Violação de regra de negócio"),
	TAMANHO_EXCEDIDO("/'tamanho-excedido", "Tamanho excedido");
	
	
	
	private String title;
	private String uri;
	
	ProblemType(String path, String title) {
		this.uri = "https://empreget.com.br" + path;
		this.title = title;
	}
	
}

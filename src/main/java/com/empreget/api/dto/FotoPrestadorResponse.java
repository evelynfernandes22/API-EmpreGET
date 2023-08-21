package com.empreget.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FotoPrestadorResponse {

	private String nomeArquivo;
	private String contentType;
	private Long tamanho;
}

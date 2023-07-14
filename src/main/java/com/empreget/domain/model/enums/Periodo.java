package com.empreget.domain.model.enums;

import lombok.Getter;

@Getter
public enum Periodo {

	MATUTINO ("Das 08:00h às 12:00h"),
	VESPERTINO ("Das 13:00h às 17:00h"),
	COMERCIAL ("Das 08:00h às 12:00h e das 13:00h às 17:00h");
	
	private String descrição;

	Periodo(String descrição) {
		this.descrição = descrição;
	}
	
	
}

package com.empreget.api.dto;

import com.empreget.domain.model.enums.UserRole;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioResponse {
	
	private Long id;
	private String email;
	private UserRole role;


}

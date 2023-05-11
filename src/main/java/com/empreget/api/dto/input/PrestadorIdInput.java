package com.empreget.api.dto.input;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrestadorIdInput {

	@NotNull
	private Long id;
}

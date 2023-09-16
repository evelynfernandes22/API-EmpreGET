package com.empreget.api.openApi.model;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageModelOpenApi<T> {
	
	private List<T> content;
	
	@ApiModelProperty(example="5", value= "Quantidade de registros por página")
	private Long size;
	@ApiModelProperty(example="10", value= "Total de registros por página")
	private Long totalElements;
	@ApiModelProperty(example="5", value= "Total de páginas")
	private Long totalPages;
	@ApiModelProperty(example="0", value= "Número da página (começa com zero)")
	private Long number;

}

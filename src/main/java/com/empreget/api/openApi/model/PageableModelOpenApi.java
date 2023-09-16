package com.empreget.api.openApi.model;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel("Pageable")
@Getter
@Setter
public class PageableModelOpenApi {

	@ApiModelProperty(example="0", value= "Número de página(começa com zero)")
	private int page;
	
	@ApiModelProperty(example="5", value= "Quantidade de elementos por página")
	private int size;
	
	@ApiModelProperty(example="nome,asc", value= "Nome da propriedade para ordenação")
	private List<String> sort;
}

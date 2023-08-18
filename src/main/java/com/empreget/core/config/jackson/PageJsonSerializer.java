package com.empreget.core.config.jackson;

import java.io.IOException;

import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.domain.Page;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@JsonComponent
public class PageJsonSerializer extends JsonSerializer<Page<?>> {

	@Override
	public void serialize(Page<?> page, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		
		gen.writeStartObject();
		
		gen.writeObjectField("conteudo", page.getContent());
		gen.writeObjectField("paginacao", page.getSize());
		gen.writeObjectField("totalElementos", page.getTotalElements());
		gen.writeObjectField("totalPages", page.getTotalPages());
		gen.writeObjectField("pageAtual", page.getNumber());
		
		gen.writeEndObject();
		
	}

}

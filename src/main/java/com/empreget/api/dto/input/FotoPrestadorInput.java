package com.empreget.api.dto.input;

import javax.validation.constraints.NotNull;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import com.empreget.core.config.validator.FileContentType;
import com.empreget.core.config.validator.FileSize;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FotoPrestadorInput {

	@ApiModelProperty(required = true)
	@NotNull
	@FileSize(max= "500KB")
	@FileContentType(allowed = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
	private MultipartFile arquivo;
	
}

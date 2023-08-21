package com.empreget.core.config.validator;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

public class FileContentTypeValidator implements ConstraintValidator<FileContentType, MultipartFile>{

	private List<String> allowedContetType;
	
	@Override
	public void initialize(FileContentType constraintAnnotation) {
		this.allowedContetType = Arrays.asList(constraintAnnotation.allowed());
	}
	
	@Override
	public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
		return multipartFile == null || this.allowedContetType.contains(multipartFile.getContentType());
	}
}

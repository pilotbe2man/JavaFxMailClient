package com.jinyuan.controller;

import com.jinyuan.model.EmailValidator;

public abstract class AbstractController {
	
	private ModelAccess modelAccess;
	private EmailValidator emailValidator;
	
	public AbstractController(ModelAccess modelAccess) {
		this.modelAccess = modelAccess;
		this.emailValidator = new EmailValidator();
	}
	
	public ModelAccess getModelAccess() {
		return modelAccess;
	}


	public boolean isValidateEmail(String aEmail) {
		return emailValidator.validate(aEmail);
	}
}

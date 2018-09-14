package com.jinyuan.controller;

public abstract class AbstractController {
	
	private ModelAccess modelAccess;

	public AbstractController(ModelAccess modelAccess) {
		this.modelAccess = modelAccess;
	}
	
	public ModelAccess getModelAccess(){
		return modelAccess;
	}

}

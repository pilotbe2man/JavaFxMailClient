package com.jinyuan.controller.persistence;

import java.io.Serializable;

public class ValidAccount implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ValidAccount(String address, String password, String type) {
		this.address = address;
		this.password = password;
		this.type = type;
	}
	private String address;
	public String getAddress() {
		return address;
	}
	private String password;
	public String getPassword() {
		return password;
	}
	private String type;
	public String getType() {
		return type;
	}

}

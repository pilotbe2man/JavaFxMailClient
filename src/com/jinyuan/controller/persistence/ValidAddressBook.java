package com.jinyuan.controller.persistence;

import java.io.Serializable;

public class ValidAddressBook implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ValidAddressBook(String address) {
		this.address = address;
	}
	private String address;
	public String getAddress() {
		return address;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return address;
	}

}

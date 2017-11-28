package com.example.service.handler;

import com.bff.core.framework.exception.ValidationResponse;

public class BusinessException extends ValidationResponse {
	
	private String addMoredetails;

	public String getAddMoredetails() {
		return addMoredetails;
	}

	public void setAddMoredetails(String addMoredetails) {
		this.addMoredetails = addMoredetails;
	}
	
	

}

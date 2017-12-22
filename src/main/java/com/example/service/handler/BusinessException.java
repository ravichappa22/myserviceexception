package com.example.service.handler;

import com.bff.core.framework.exception.ValidationResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusinessException extends ValidationResponse {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty
	private String addMoredetails;

	public String getAddMoredetails() {
		return addMoredetails;
	}

	public void setAddMoredetails(String addMoredetails) {
		this.addMoredetails = addMoredetails;
	}
	
	

}

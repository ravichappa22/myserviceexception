package com.example.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bff.core.framework.exception.FrameworkValidationError;
import com.example.service.FeignServiceImplementation;

@RestController
public class MyFeignController {
	
	@Autowired
	private FeignServiceImplementation feignServiceClient;
	
	@RequestMapping(value="/feignname", produces="application/json")
	//@ExceptionHandler(FrameworkValidationError.class)
	public String getFeignResponse(){
		return feignServiceClient.getMyName();
	}
	
	@RequestMapping(value="/anothername", produces="application/json")
	public String getException(){
		return feignServiceClient.getException();
	}

}

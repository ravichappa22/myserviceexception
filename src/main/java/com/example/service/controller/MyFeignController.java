package com.example.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.FeignServiceImplementation;

@RestController
public class MyFeignController {
	
	@Autowired
	private FeignServiceImplementation feignServiceClient;
	
	@RequestMapping(value="/feignname", produces="application/json")
	public String getFeignResponse(){
		return feignServiceClient.getMyName();
	}

}

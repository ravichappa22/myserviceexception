package com.example.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value="myrestfeign", url="http://localhost:9090")
public interface FeignInterface {
	
	@RequestMapping(value="/name", method = RequestMethod.GET)
	public String getMyName(@RequestHeader("Authorization") String authorization);
}

package com.example.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value="myrestfeign", url="http://localhost:8060")
public interface MyAnotherServiceClient {

	@RequestMapping("/anothername")
	public String getAnotherRestService();
}

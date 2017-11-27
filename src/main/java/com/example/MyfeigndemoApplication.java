package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

import com.example.service.FeignInterface;
import com.example.service.MyAnotherServiceClient;

@SpringBootApplication
@EnableFeignClients(basePackageClasses = {FeignInterface.class, MyAnotherServiceClient.class})
public class MyfeigndemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyfeigndemoApplication.class, args);
	}
	
	@Bean(name = "MessageSource")
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource obj = new ResourceBundleMessageSource();
		obj.setBasename("bundles/messages");
		return obj;
	}
}

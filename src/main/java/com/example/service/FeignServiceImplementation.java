package com.example.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.bff.core.framework.exception.FrameworkValidationError;
import com.bff.core.framework.exception.Message;
import com.bff.core.framework.exception.ServiceException;

import feign.FeignException;

@Service
public class FeignServiceImplementation {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FeignServiceImplementation.class);

	@Autowired
	private FeignInterface feignInterface;
	
	@Autowired
	private MyAnotherServiceClient myAnotherServiceClient;
	
	private String authorization;
	
	public String getMyName(){
		String response = null;
		// add exception logic here
		try {
			response = feignInterface.getMyName(authorization);
			LOGGER.info("debug log calling service");
			//throw new FrameworkValidationError("1000");
			
		} catch (FeignException  e) {
			LOGGER.error("Exception Occured in feign service impl" );
			Map<String, Object> details = new HashMap<>();
			details.put("claimNumber", "1234567898");
			details.put("generator", "RestService");
			details.put("sourceOfError", "MemberValidation");
			FrameworkValidationError frameworkValidationError = new FrameworkValidationError("1000", details, e);
			
			//create detailed messages
			Message exceptionMessage = new Message();
			exceptionMessage.setCode("1001");
			String[] subs = new String[1];
			subs[0] = "123456789";
			exceptionMessage.setArguments(subs);
			List<Message> messageList = new ArrayList<Message>();
			messageList.add(exceptionMessage);
			frameworkValidationError.setValidationMessages(messageList);
			
			throw frameworkValidationError;
		
			
		}
		return response;
	}
	
	
	
	public String getException(){
		return myAnotherServiceClient.getAnotherRestService();
	}
}

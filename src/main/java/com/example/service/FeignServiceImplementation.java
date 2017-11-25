package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bff.core.framework.exception.FrameworkError;
import com.bff.core.framework.exception.FrameworkValidationError;
import com.bff.core.framework.exception.Message;

@Service
public class FeignServiceImplementation {

	@Autowired
	private FeignInterface feignInterface;
	
	private String authorization="bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MTA5NDUyMzEsInVzZXJfbmFtZSI6InNlcnZpY2UtYWNjb3VudC0xIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV93cml0ZSIsIlJPTEVfcmVhZCJdLCJqdGkiOiIwYzU0ZTkyMC02YTAxLTQxMDUtODExZi01ZjQ4OWMzZGRjMDciLCJjbGllbnRfaWQiOiJzZXJ2aWNlLWFjY291bnQtMSIsInNjb3BlIjpbXX0.KzjcUN9TU8SHqOztE8VL8cTBgyNgyl7JeghlEsBPiVa9DaJHq-EmrLEaD8e169qfdGRspb6DJ9t4oe6Q4asKwAtvQtarZaboStgbOXuC8PCM20gLvHAjdyP7sGgVzSFKKoM7haGtqoAUhzeOqoWlsIfkZg9qJeowFWH9LJ0F9w6gS69bPj1lOhxBOEGWz_DM8S_ylxJU9QUY3Cbb8nNM4QaLmrAXvV1AUtrjWvGqXCyaeTb6yd6cQP7Wqvu4jwtezOu3orpp28AwlDvgzfexHcEtPI2cRLff_WAESdv1MExll-ylSilLpVx8lwxFbtk3es0ThKrBGDHIXXSCN97Jhg";
	
	public String getMyName(){
		String response = null;
		// add exception logic here
		try {
			response = feignInterface.getMyName(authorization);
		} catch (Exception e) {
			//create top error
			Map<String, Object> details = new HashedMap();
			details.put("claimNumber", "123456");
			details.put("generator", "RestService");
			details.put("sourceOfError", "MemberValidation");
			FrameworkValidationError frameworkValidationError = new FrameworkValidationError("1000", details, e);
			
			//create detailed messages
			Message exceptionMessage = new Message();
			exceptionMessage.setCode("1001");
			String[] subs = new String[1];
			subs[0] = "123456" + e.getMessage();
			exceptionMessage.setArguments(subs);
			List<Message> messageList = new ArrayList<Message>();
			messageList.add(exceptionMessage);
			
			
			
			frameworkValidationError.setValidationMessages(messageList);
			
			throw frameworkValidationError;
		}
		return response;
	}
}

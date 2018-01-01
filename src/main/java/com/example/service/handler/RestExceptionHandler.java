package com.example.service.handler;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.bff.core.framework.exception.ErrorResponse;
import com.bff.core.framework.exception.ExceptionAndValidatorUtils;
import com.bff.core.framework.exception.FrameworkError;
import com.bff.core.framework.exception.FrameworkValidationError;
import com.bff.core.framework.exception.Message;
import com.bff.core.framework.exception.ServiceMessage;
import com.bff.core.framework.exception.ValidationResponse;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

	@Autowired
	private ExceptionAndValidatorUtils exceptionAndValidatorUtils;

	/**
	 * Handles exception.
	 *
	 * @param WebRequest
	 *            the req
	 * @param Exception
	 *            the ex
	 * @return the response entity Object
	 */
	@ExceptionHandler(Exception.class)
	ResponseEntity<Object> handleException(WebRequest req, Exception ex) {
		LOGGER.info("RestExceptionHandler::handleException::Start");
		ErrorResponse errorResponse = new ErrorResponse(ex);
		LOGGER.info("RestExceptionHandler::handleException::End");
		return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), errorResponse.getStatus(), req);
	}

	/**
	 * Handles framework validation error.
	 *
	 * @param FrameworkValidationError
	 *            the ex
	 * @param WebRequest
	 *            the request
	 * @return the response entity object
	 */
	@ExceptionHandler(FrameworkValidationError.class)
	public ResponseEntity<Object> handleFrameworkValidationError(FrameworkValidationError ex, WebRequest request) {
		LOGGER.info("RestExceptionHandler::handleFrameworkValidationError::Start");
		BusinessException validationResponse = new BusinessException();
		Message message = ex.getErrorMessage();
		if (message != null) {
			validationResponse.setCode(ex.getErrorMessage().getCode());
			validationResponse.setMessage(exceptionAndValidatorUtils.resolveErrorText(message));
		}
		List<Message> validationMessages = ex.getValidationMessages();
		if (validationMessages != null) {
			List<ServiceMessage> validationMsges = new ArrayList<ServiceMessage>();
			for (Message msg : validationMessages) {
				ServiceMessage serviceMessage = new ServiceMessage();
				serviceMessage.setText(exceptionAndValidatorUtils.resolveErrorText(msg));
				try {
					serviceMessage.setCode(Long.parseLong(msg.getCode()));
				} catch (Exception e) {
					LOGGER.error("Error Code cannot be Parsed" + msg.getCode());
				}
				validationMsges.add(serviceMessage);
			}
			validationResponse.addValidationMessages(validationMsges);
		}
		validationResponse.setDetailMap(ex.getDetails());
		//validationResponse.setType(ex.getClass().getCanonicalName());
		validationResponse.setStatus(HttpStatus.BAD_REQUEST);
		validationResponse.setAddMoredetails("Exception generated by myserviceException");
		LOGGER.info("RestExceptionHandler::handleFrameworkValidationError::End");
		
		return handleExceptionInternal(ex, validationResponse, new HttpHeaders(), validationResponse.getStatus(),
				request);
	}

	/**
	 * Handles framework error.
	 *
	 * @param FrameworkError
	 *            the ex
	 * @param WebRequest
	 *            the request
	 * @return the response entity object
	 */
	@ExceptionHandler(FrameworkError.class)
	public ResponseEntity<Object> handleFrameworkError(FrameworkError ex, WebRequest request) {
		LOGGER.info("RestExceptionHandler::handleFrameworkError::Start");
		ErrorResponse errorResponse = new ErrorResponse();
		Message message = ex.getErrorMessage();
		if (message != null) {
			errorResponse.setCode(ex.getErrorMessage().getCode());
			errorResponse.setMessage(exceptionAndValidatorUtils.resolveErrorText(message));
		}
		errorResponse.setDetails(ex.getDetails());
		//errorResponse.setType(ex.getClass().getCanonicalName());
		LOGGER.info("RestExceptionHandler::handleFrameworkError::End");
		return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), errorResponse.getStatus(), request);
	}

	/**
	 * Default excetion handler
	 * 
	 * @param Exception
	 *            the ex
	 * @param WebRequest
	 *            the request
	 * @param Object
	 *            as body
	 * @param HttpHeaders
	 *            as headers
	 * @return the response entity object
	 */
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		LOGGER.info("Exception occurred for req[" + request + "]   ex: " + ex.toString(), ex);
		if (request instanceof ServletWebRequest) {
			ServletWebRequest servletWebRequest = (ServletWebRequest) request;
			LOGGER.info("Request Path: " + servletWebRequest.getRequest().getRequestURI());
		}
		if (body instanceof ErrorResponse) {
			ErrorResponse errorResponse = (ErrorResponse) body;
			LOGGER.info("ErrorResponse: HttpStatus[" + errorResponse.getStatus() + "]  code[" + errorResponse.getCode()
					+ "]  message[" + errorResponse.getMessage() + "]   timestamp[" + errorResponse.getTimestamp()
					+ "]  details[" + errorResponse.getDetails() + "]");
		}
		if (body instanceof ValidationResponse) {
			ValidationResponse validationResponse = (ValidationResponse) body;
			LOGGER.info("ErrorResponse: HttpStatus[" + validationResponse.getStatus() + "]  code["
					+ validationResponse.getCode() + "]  message[" + validationResponse.getMessage() + "]   timestamp["
					+ validationResponse.getTimestamp() + "]  details[" + validationResponse.getDetailMap() + "]");
		}
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}

}

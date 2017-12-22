package com.example.service.handler;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import com.bff.core.framework.exception.FrameworkError;
import com.bff.core.framework.exception.FrameworkValidationError;
import com.bff.core.framework.exception.Message;

@Component
public class ExceptionAndValidatorUtils {
	private static final Logger log = LoggerFactory.getLogger(ExceptionAndValidatorUtils.class);

	@Resource(name = "MessageSource")
	private ResourceBundleMessageSource messageSource;

	public Errors getErrorsInstance(Object target) {
		return new BindException(target, "name");
	}

	public List<Message> convertErrors(Errors objectErrors) {
		List<Message> msgs = new ArrayList<Message>();
		List<ObjectError> allErrors = objectErrors.getAllErrors();
		for (ObjectError objError : allErrors) {

			
			Object[] errorArgs = objError.getArguments();
			if (errorArgs != null && errorArgs.length > 0 && errorArgs[0] != null
					&& errorArgs[0].getClass().isArray()) {
				errorArgs = (Object[]) errorArgs[0];
			}
			msgs.add(new Message(objError.getCode(), errorArgs, objError.getDefaultMessage()));
		}
		return msgs;
	}

	public void convertAndThrow(Errors objectErrors) {
		if (objectErrors.hasErrors()) {
			List<Message> validationMessages = convertErrors(objectErrors);
			FrameworkValidationError frameworkValidationError = new FrameworkValidationError(null);
			frameworkValidationError.setValidationMessages(validationMessages);
			throw frameworkValidationError;
		}
	}

	public void resolveErrorMessages(Exception exception) {
		if (exception instanceof FrameworkError) {
			FrameworkError frameworkError = (FrameworkError) exception;
			Message message = frameworkError.getErrorMessage();
			resolveErrorText(message);
		}

		if (exception instanceof FrameworkValidationError) {
			FrameworkValidationError frameworkValidationError = (FrameworkValidationError) exception;
			List<Message> validationMessages = frameworkValidationError.getValidationMessages();
			if (validationMessages != null) {
				for (Message message : validationMessages) {
					resolveErrorText(message);
				}
			}
		}
	}

	public String resolveErrorText(Message errorMessage) {
		String text = errorMessage.getText();
		if (text == null) {
			if (messageSource != null) {
				try {
					/**
					 * we are going to read this from a remote cache and that cache will be filled at the time of application deployment
					 * The key for that cache is message code. below code first looks at the remote cache, then looks locally.
					 */
					
					text = messageSource.getMessage(errorMessage.getCode(), errorMessage.getArguments(),
							Locale.getDefault());
				} catch (NoSuchMessageException e) {
					log.debug("failed to look up message", e);
				}
			}
			if (text == null) {
				String defaultMessage = errorMessage.getDefaultMessage();
				if (defaultMessage != null) {
					text = MessageFormat.format(defaultMessage, errorMessage.getArguments());
				}
			}
			errorMessage.setText(text);
		}
		return text;
	}

}

package com.school.sba.exception_handlers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.school.sba.exceptions.ContraintsValidationException;
import com.school.sba.exceptions.ExistingAdminException;
import com.school.sba.exceptions.UserNotFoundByIdException;

@RestControllerAdvice
public class ApplicationExceptionHandler {

	public ResponseEntity<Object> structre(HttpStatus status, String message, Object rootCause) {
		return new ResponseEntity<Object>(
				Map.of("status", HttpStatus.BAD_REQUEST.value(), "message", message, "rootcause", rootCause),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ContraintsValidationException.class)
	public ResponseEntity<Object> contraintsValidationException(ContraintsValidationException ex) {
		return structre(HttpStatus.BAD_REQUEST, ex.getMessage(), "no duplicate values for phone number and email ");
	}

	@ExceptionHandler(ExistingAdminException.class)
	public ResponseEntity<Object> existingAdminException(ExistingAdminException ex) {
		return structre(HttpStatus.BAD_REQUEST, ex.getMessage(), "Admin already present no duplicate admin ");
	}
	
	@ExceptionHandler(UserNotFoundByIdException.class)
	public ResponseEntity<Object> userNotFoundByIdException(UserNotFoundByIdException ex) {
		return structre(HttpStatus.NOT_FOUND, ex.getMessage(), "user not found with the specified user ID  ");
	}
}

package com.interview.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthException {
	   @ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleAll(Exception e){
		return new ResponseEntity<>(e.getMessage() , HttpStatus.UNAUTHORIZED);
	}
}

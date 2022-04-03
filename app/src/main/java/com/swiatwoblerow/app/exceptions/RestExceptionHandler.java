package com.swiatwoblerow.app.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler{
	
	
	@ExceptionHandler(NotFoundException.class)
	protected ResponseEntity<Object> handleEntityNotFound(
			NotFoundException ex){
		return new ResponseEntity<>(ex,ex.getStatus());
	}
	
	@ExceptionHandler(MyUsernameNotFoundException.class)
	protected ResponseEntity<Object> handleUsernameNotFoundException(
			MyUsernameNotFoundException ex){
		return new ResponseEntity<>(ex,ex.getStatus());
	}
	
//	private ResponseEntity<Object> buildResponseEntity(NotFoundException ex){
//		return new ResponseEntity<>(ex,ex.getStatus());
//	}
}

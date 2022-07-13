package com.swiatwoblerow.app.exceptions;

import java.sql.Timestamp;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler{
	
	
	@ExceptionHandler(value= {NotFoundExceptionRequest.class, UsernameNotFoundException.class})
	protected ResponseEntity<Object> handleEntityNotFound(
			Exception ex){
		NotFoundException exception = new NotFoundException(
				ex.getMessage(),
				new Timestamp(System.currentTimeMillis()));
		return new ResponseEntity<>(exception,exception.getStatus());
	}
	
//	@ExceptionHandler(UsernameNotFoundException.class)
//	protected ResponseEntity<Object> handleUsernameNotFoundException(
//			UsernameNotFoundException ex){
//		NotFoundException exception = new NotFoundException(
//				ex.getMessage(),
//				new Timestamp(System.currentTimeMillis()));
//		return new ResponseEntity<>(exception,exception.getStatus());
//	}
	
//	private ResponseEntity<Object> buildResponseEntity(NotFoundException ex){
//		return new ResponseEntity<>(ex,ex.getStatus());
//	}
}

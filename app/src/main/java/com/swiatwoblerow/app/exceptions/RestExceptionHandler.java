package com.swiatwoblerow.app.exceptions;

import java.sql.Timestamp;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.swiatwoblerow.app.exceptions.dto.BadCredentialsExceptionDto;
import com.swiatwoblerow.app.exceptions.dto.NotFoundExceptionDto;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler{
	
	
	@ExceptionHandler(value= {NotFoundExceptionRequest.class, UsernameNotFoundException.class})
	protected ResponseEntity<Object> handleEntityNotFound(
			Exception ex){
		NotFoundExceptionDto exception = new NotFoundExceptionDto(
				ex.getMessage(),
				new Timestamp(System.currentTimeMillis()));
		return new ResponseEntity<>(exception,exception.getStatus());
	}
	
	@ExceptionHandler(value= {BadCredentialsException.class})
	protected ResponseEntity<Object> handleCustomBadCredentials(
			Exception ex){
		BadCredentialsExceptionDto exception = new BadCredentialsExceptionDto(
				ex.getMessage(),
				new Timestamp(System.currentTimeMillis()));
		return new ResponseEntity<>(exception,exception.getStatus());
	}
}

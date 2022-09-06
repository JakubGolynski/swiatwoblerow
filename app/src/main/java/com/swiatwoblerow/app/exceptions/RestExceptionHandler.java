package com.swiatwoblerow.app.exceptions;

import java.sql.Timestamp;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.swiatwoblerow.app.exceptions.dto.AlreadyExistsExceptionDto;
import com.swiatwoblerow.app.exceptions.dto.BadCredentialsExceptionDto;
import com.swiatwoblerow.app.exceptions.dto.CustomerIsNotOwnerExceptionDto;
import com.swiatwoblerow.app.exceptions.dto.NotFoundExceptionDto;
import com.swiatwoblerow.app.exceptions.dto.NullArgumentExceptionDto;
import com.swiatwoblerow.app.exceptions.dto.TooManyInsertExceptionDto;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler{
	
	
	@ExceptionHandler(value= {NotFoundExceptionRequest.class, UsernameNotFoundException.class, NullPointerException.class})
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
	
	@ExceptionHandler(value= {TooManyInsertException.class})
	protected ResponseEntity<Object> handleTooManyInsert(Exception ex){
		TooManyInsertExceptionDto exception = new TooManyInsertExceptionDto(
				ex.getMessage(), new Timestamp(System.currentTimeMillis()));
		return new ResponseEntity<>(exception, exception.getStatus());
	}
	
	@ExceptionHandler(value= {CustomerIsNotOwnerException.class})
	protected ResponseEntity<Object> handleCustomerIsNotOwner(Exception ex){
		CustomerIsNotOwnerExceptionDto exception = new CustomerIsNotOwnerExceptionDto(
				ex.getMessage(), new Timestamp(System.currentTimeMillis()));
		return new ResponseEntity<>(exception, exception.getStatus());
	}
	
	@ExceptionHandler(value= {AlreadyExistsException.class})
	protected ResponseEntity<Object> handleAlreadyExists(Exception ex){
		AlreadyExistsExceptionDto exception = new AlreadyExistsExceptionDto(
				ex.getMessage(), new Timestamp(System.currentTimeMillis()));
		return new ResponseEntity<>(exception, exception.getStatus());
	}
	
	@ExceptionHandler(value= {NullArgumentException.class})
	protected ResponseEntity<Object> handleNullArgument(Exception ex){
		NullArgumentExceptionDto exception = new NullArgumentExceptionDto(
				ex.getMessage(), new Timestamp(System.currentTimeMillis()));
		return new ResponseEntity<>(exception, exception.getStatus());
	}
}

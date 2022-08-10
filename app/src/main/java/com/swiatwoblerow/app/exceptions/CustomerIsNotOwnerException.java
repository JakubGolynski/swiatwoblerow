package com.swiatwoblerow.app.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class CustomerIsNotOwnerException extends Exception{
	
	private HttpStatus status = HttpStatus.CONFLICT;

	public CustomerIsNotOwnerException(String message) {
		super(message);
	}
}

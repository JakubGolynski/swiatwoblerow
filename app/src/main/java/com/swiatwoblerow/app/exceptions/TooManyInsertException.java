package com.swiatwoblerow.app.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class TooManyInsertException extends Exception{
	
	private HttpStatus status = HttpStatus.CONFLICT;

	public TooManyInsertException(String message) {
		super(message);
	}
}

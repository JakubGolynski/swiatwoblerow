package com.swiatwoblerow.app.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class AlreadyExistsException extends Exception{

	private HttpStatus status = HttpStatus.CONFLICT;

	public AlreadyExistsException(String message) {
		super(message);
	}

}

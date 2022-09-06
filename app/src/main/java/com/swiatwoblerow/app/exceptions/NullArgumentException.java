package com.swiatwoblerow.app.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class NullArgumentException extends Exception{

	private HttpStatus status = HttpStatus.CONFLICT;

	public NullArgumentException(String message) {
		super(message);
	}
}

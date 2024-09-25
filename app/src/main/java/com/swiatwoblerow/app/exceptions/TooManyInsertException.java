package com.swiatwoblerow.app.exceptions;

import org.springframework.http.HttpStatus;


public class TooManyInsertException extends Exception{
	
	private HttpStatus status = HttpStatus.CONFLICT;

	public TooManyInsertException(String message) {
		super(message);
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
	
}

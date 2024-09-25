package com.swiatwoblerow.app.exceptions;

import org.springframework.http.HttpStatus;


public class NullArgumentException extends Exception{

	private HttpStatus status = HttpStatus.CONFLICT;

	public NullArgumentException(String message) {
		super(message);
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
	
	
}
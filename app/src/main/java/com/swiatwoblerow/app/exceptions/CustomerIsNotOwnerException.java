package com.swiatwoblerow.app.exceptions;

import org.springframework.http.HttpStatus;


public class CustomerIsNotOwnerException extends Exception{
	
	private HttpStatus status = HttpStatus.CONFLICT;

	public CustomerIsNotOwnerException(String message) {
		super(message);
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
	
	
}

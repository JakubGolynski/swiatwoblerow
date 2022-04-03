package com.swiatwoblerow.app.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends Exception{
	
	private HttpStatus status = HttpStatus.NOT_FOUND;

	public NotFoundException(String message) {
		super(message);
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
	
}

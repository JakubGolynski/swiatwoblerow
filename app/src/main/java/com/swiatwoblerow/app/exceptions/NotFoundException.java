package com.swiatwoblerow.app.exceptions;

import java.sql.Timestamp;

import org.springframework.http.HttpStatus;

public class NotFoundException{
	
	private HttpStatus status = HttpStatus.NOT_FOUND;
	
	private String message;
	
	private Timestamp timestamp;

	public NotFoundException(String message, Timestamp timestamp) {
		this.message = message;
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
	
}

package com.swiatwoblerow.app.exceptions.dto;

import java.sql.Timestamp;

import org.springframework.http.HttpStatus;


public class BadCredentialsExceptionDto{
	
	private HttpStatus status = HttpStatus.UNAUTHORIZED;
	
	private String message;
	
	private Timestamp timestamp;
	
	public BadCredentialsExceptionDto(String message, Timestamp timestamp) {
		this.message = message;
		this.timestamp = timestamp;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
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
	
	
	
}

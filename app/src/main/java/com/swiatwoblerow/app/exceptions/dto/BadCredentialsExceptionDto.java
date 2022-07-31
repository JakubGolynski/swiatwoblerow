package com.swiatwoblerow.app.exceptions.dto;

import java.sql.Timestamp;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class BadCredentialsExceptionDto{
	
	private HttpStatus status = HttpStatus.UNAUTHORIZED;
	
	private String message;
	
	private Timestamp timestamp;
	
	public BadCredentialsExceptionDto(String message, Timestamp timestamp) {
		this.message = message;
		this.timestamp = timestamp;
	}
	
}

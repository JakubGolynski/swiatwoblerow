package com.swiatwoblerow.app.exceptions.dto;

import java.sql.Timestamp;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class NullArgumentExceptionDto {

private HttpStatus status = HttpStatus.CONFLICT;
	
	private String message;
	
	private Timestamp timestamp;

	public NullArgumentExceptionDto(String message, Timestamp timestamp) {
		this.message = message;
		this.timestamp = timestamp;
	}
}

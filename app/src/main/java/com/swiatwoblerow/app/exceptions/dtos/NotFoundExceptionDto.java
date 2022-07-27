package com.swiatwoblerow.app.exceptions.dtos;

import java.sql.Timestamp;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class NotFoundExceptionDto{
	
	private HttpStatus status = HttpStatus.NOT_FOUND;
	
	private String message;
	
	private Timestamp timestamp;

	public NotFoundExceptionDto(String message, Timestamp timestamp) {
		this.message = message;
		this.timestamp = timestamp;
	}

}

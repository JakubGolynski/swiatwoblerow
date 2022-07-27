package com.swiatwoblerow.app.exceptions;


import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class NotFoundExceptionRequest extends Exception{
	
	private HttpStatus status = HttpStatus.NOT_FOUND;

	public NotFoundExceptionRequest(String message) {
		super(message);
	}

}

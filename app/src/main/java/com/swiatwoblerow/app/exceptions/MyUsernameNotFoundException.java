package com.swiatwoblerow.app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MyUsernameNotFoundException extends UsernameNotFoundException{
	
	private HttpStatus status = HttpStatus.NOT_FOUND;
	
	public MyUsernameNotFoundException(String message) {
		super(message);
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
}

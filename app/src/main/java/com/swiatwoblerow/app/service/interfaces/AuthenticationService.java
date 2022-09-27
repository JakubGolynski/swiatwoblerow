package com.swiatwoblerow.app.service.interfaces;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.swiatwoblerow.app.dto.CustomerDto;

public interface AuthenticationService {
	public CustomerDto login(CustomerDto customerDto) throws BadCredentialsException,UsernameNotFoundException;
}

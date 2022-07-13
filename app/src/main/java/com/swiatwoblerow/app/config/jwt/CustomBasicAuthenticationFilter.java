package com.swiatwoblerow.app.config.jwt;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class CustomBasicAuthenticationFilter extends BasicAuthenticationFilter {

	public CustomBasicAuthenticationFilter(AuthenticationManager authenticationManager,
			AuthenticationEntryPoint authenticationEntryPoint) {
		super(authenticationManager, authenticationEntryPoint);
		// TODO Auto-generated constructor stub
	}

	public CustomBasicAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
		// TODO Auto-generated constructor stub
	
	}
	
}

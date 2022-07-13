package com.swiatwoblerow.app.config.jwt;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {
		
		response.addHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName() + "\"");
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You have to log in");

	}

	@Override
	public void afterPropertiesSet() {
		setRealmName("Realm");
        super.afterPropertiesSet();
	}
	
}

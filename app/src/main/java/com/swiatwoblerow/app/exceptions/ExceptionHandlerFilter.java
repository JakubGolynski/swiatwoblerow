package com.swiatwoblerow.app.exceptions;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.filter.OncePerRequestFilter;

public class ExceptionHandlerFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		 try {
	            filterChain.doFilter(request, response);
	     }catch (BadCredentialsException e) {

	            // custom error response class used across my project
	        	response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "ExceptionFilter");
	     }

	}

}
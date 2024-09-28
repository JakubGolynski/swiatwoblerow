package com.swiatwoblerow.app.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.swiatwoblerow.app.service.interfaces.CustomerService;

public class JWTAuthorizationFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private CustomerService customerService;

	public JWTAuthorizationFilter() {
		
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String token = parseJwt(request);
			if(token != null && jwtUtils.validateJwtToken(token)) {
				String username = jwtUtils.getUserNameFromJwtToken(token);
				UserDetails userDetails = customerService.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken authentication = 
						new UsernamePasswordAuthenticationToken(
							userDetails,null,userDetails.getAuthorities());
				authentication.setDetails(
						new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}catch(Exception e) {
			logger.error("Cannot set user authentication");
		}
		filterChain.doFilter(request, response);
	}

	private String parseJwt(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		if(header != null && header.startsWith("Bearer ")) {
			return header.substring(7,header.length());
		}
		return null;
	}

}

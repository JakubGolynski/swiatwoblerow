package com.swiatwoblerow.app.config.jwt;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import java.util.Date;

@Component
public class JwtUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
	
	@Value("${jwt.secretKey}")
	private String secretKey;
	
	@Value("${jwt.expirationTime}")
	private long expirationTime;
	
	public boolean validateJwtToken(String token){
		
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return true;
		}catch(ExpiredJwtException e) {
			logger.error("Token has exipired",e.getMessage());
		}catch(UnsupportedJwtException e) {
			logger.error("Token is unsupported",e.getMessage());
		}catch(MalformedJwtException e) {
			logger.error("Inwalid JWT token",e.getMessage());
		}catch(SignatureException e) {
			logger.error("Inwalid JWT token",e.getMessage());
		}catch(IllegalArgumentException e) {
			logger.error("Inwalid JWT token",e.getMessage());
		}
		return false;
	}
	
	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser()
					.setSigningKey(secretKey)
					.parseClaimsJws(token)
					.getBody()
					.getSubject();
	}
	
	public String generateJwtToken(String username) {
		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis()+expirationTime))
				.signWith(SignatureAlgorithm.HS512, secretKey)
				.compact();
	}
}

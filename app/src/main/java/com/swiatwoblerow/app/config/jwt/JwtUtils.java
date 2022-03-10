package com.swiatwoblerow.app.config.jwt;

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
	
	@Value("${jwt.secretKey}")
	private String secretKey;
	
	@Value("${jwt.expirationTime}")
	private long expirationTime;
	
	public boolean validateJwtToken(String token){
		
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return true;
		}catch(ExpiredJwtException e) {
//			logger.error("ss",e.getMessage());
			
		}catch(UnsupportedJwtException e) {
//			logger.
		}catch(MalformedJwtException e) {
//			logger.error("ss",e.getMessage());
		}catch(SignatureException e) {
//			logger.error("ss",e.getMessage());
		}catch(IllegalArgumentException e) {
//			logger.error("ss",e.getMessage());
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

package com.swiatwoblerow.app.dto;

import java.util.ArrayList;
import java.util.List;

public class CustomerDto {
	
	private String username;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String accessToken;

	private String tokenType;
	
	private List<String> roles = new ArrayList<>();

	public CustomerDto() {

	}

	public CustomerDto(String username, String firstName, String lastName, String email, String accessToken,
			String tokenType, List<String> roles) {
		super();
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.accessToken = accessToken;
		this.tokenType = tokenType;
		this.roles = roles;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
}

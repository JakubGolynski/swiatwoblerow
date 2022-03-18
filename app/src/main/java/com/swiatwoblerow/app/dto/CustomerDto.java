package com.swiatwoblerow.app.dto;

public class CustomerDto {
	
	private String username;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String accessToken;

	private String tokenType;
	
	public CustomerDto() {

	}

	public CustomerDto(String username, String firstName, String lastName, String email,
			String accessToken, String tokenType) {
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.accessToken = accessToken;
		this.tokenType = tokenType;
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
	
}

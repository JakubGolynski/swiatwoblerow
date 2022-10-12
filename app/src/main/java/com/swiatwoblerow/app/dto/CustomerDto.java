package com.swiatwoblerow.app.dto;


import javax.validation.constraints.NotEmpty;

import lombok.Data;


@Data
public class CustomerDto {
	
	private Integer id;
	
	@NotEmpty(message="username cannot be null or empty")
	private String username;
	
	private String password;
	
	@NotEmpty(message="first name cannot be null or empty")
	private String firstName;
	
	@NotEmpty(message="last name cannot be null or empty")
	private String lastName;
	
	@NotEmpty(message="email cannot be null or empty")
	private String email;
	
	private String telephone;
	
	private String jwtToken;
	
	private AddressDto address;
	
	private RoleDto role;

	public CustomerDto() {

	}

	public CustomerDto(String username, String password, String firstName, String lastName, String email,
			String telephone, String jwtToken, AddressDto address, RoleDto role) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.telephone = telephone;
		this.jwtToken = jwtToken;
		this.address = address;
		this.role = role;
	}
	
}

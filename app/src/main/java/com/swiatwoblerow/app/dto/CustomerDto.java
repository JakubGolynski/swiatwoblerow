package com.swiatwoblerow.app.dto;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;


@Data
public class CustomerDto {
	
	private String username;
	
	private String password;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String telephone;
	
	private String jwtToken;
	
	private AddressDto address;
	
	private Set<RoleDto> roles = new HashSet<>();

	public CustomerDto() {

	}

	public CustomerDto(String username, String password, String firstName, String lastName, String email,
			String telephone, String jwtToken, AddressDto address, Set<RoleDto> roles) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.telephone = telephone;
		this.jwtToken = jwtToken;
		this.address = address;
		this.roles = roles;
	}
	
}

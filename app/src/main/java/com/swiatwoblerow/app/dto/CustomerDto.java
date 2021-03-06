package com.swiatwoblerow.app.dto;

import java.util.ArrayList;
import java.util.List;

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
	
	private AddressDto customerAddress;
	
	private List<String> roles = new ArrayList<>();

	public CustomerDto() {

	}

	public CustomerDto(String username, String password, String firstName, String lastName, String email,
			String telephone, String jwtToken, AddressDto customerAddress, List<String> roles) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.telephone = telephone;
		this.jwtToken = jwtToken;
		this.customerAddress = customerAddress;
		this.roles = roles;
	}
	
}

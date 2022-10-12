package com.swiatwoblerow.app.service.filter;


import lombok.Data;
@Data
public class CustomerFilter {
	
	private String firstName;
	
	private String lastName;
	
	private String username;
	
	private String city;

	private String email;
	
	private Integer page = 0;
	
	private Integer size = 50;
	
	private String sort = "username";
	
	public CustomerFilter(String firstName, String lastName, String username, String city, String email, Integer size,
			Integer page, String sort) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.city = city;
		this.email = email;
		this.size = size;
		this.page = page;
		this.sort = sort;
	}

	public CustomerFilter() {
		
	}

}

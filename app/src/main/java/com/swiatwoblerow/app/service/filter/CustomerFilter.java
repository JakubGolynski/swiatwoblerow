package com.swiatwoblerow.app.service.filter;

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	
	

}

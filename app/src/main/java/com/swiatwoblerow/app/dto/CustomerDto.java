package com.swiatwoblerow.app.dto;


import jakarta.validation.constraints.NotEmpty;


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
			String telephone, String jwtToken, AddressDto address) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.telephone = telephone;
		this.jwtToken = jwtToken;
		this.address = address;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getJwtToken() {
		return jwtToken;
	}

	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}

	public AddressDto getAddress() {
		return address;
	}

	public void setAddress(AddressDto address) {
		this.address = address;
	}

}

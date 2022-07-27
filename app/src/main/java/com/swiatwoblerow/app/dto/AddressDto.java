package com.swiatwoblerow.app.dto;

import lombok.Data;

@Data
public class AddressDto {
	
	private int id;
	
	private String city;
	
	private String street;
	
	private String houseNumber;
	
	private String country;
	
	public AddressDto() {
		
	}

	public AddressDto(int id, String city, String street, String houseNumber, String country) {
		this.id = id;
		this.city = city;
		this.street = street;
		this.houseNumber = houseNumber;
		this.country = country;
	}
	
}

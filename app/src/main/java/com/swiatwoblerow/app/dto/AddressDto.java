package com.swiatwoblerow.app.dto;

import lombok.Data;

@Data
public class AddressDto {
	
	private Integer id;
	
	private String city;
	
	private String street;
	
	private String houseNumber;
	
	private CountryDto country;
	
	public AddressDto() {
		
	}

	public AddressDto(Integer id, String city, String street, String houseNumber, CountryDto country) {
		this.id = id;
		this.city = city;
		this.street = street;
		this.houseNumber = houseNumber;
		this.country = country;
	}
	
}

package com.swiatwoblerow.app.dto;

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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public CountryDto getCountry() {
		return country;
	}

	public void setCountry(CountryDto country) {
		this.country = country;
	}
	
	
	
}

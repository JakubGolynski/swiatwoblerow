package com.swiatwoblerow.app.dto;

import lombok.Data;

@Data
public class CountryDto {
	
	private Integer id;
	
	private String name;
	
	public CountryDto() {
		
	}
	
	public CountryDto(Integer id, String name) {
		this.id = id;
		this.name = name;
	}
}

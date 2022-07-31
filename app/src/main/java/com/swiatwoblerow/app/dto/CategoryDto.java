package com.swiatwoblerow.app.dto;

import lombok.Data;

@Data
public class CategoryDto {
	
	private int id;
	
	private String name;
	
	public CategoryDto() {
		
	}
	
	public CategoryDto(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
}

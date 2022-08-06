package com.swiatwoblerow.app.dto;

import lombok.Data;

@Data
public class CategoryDto {
	
	private Integer id;
	
	private String name;
	
	public CategoryDto() {
		
	}
	
	public CategoryDto(Integer id, String name) {
		this.id = id;
		this.name = name;
	}
	
}

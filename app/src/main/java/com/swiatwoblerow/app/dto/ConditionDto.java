package com.swiatwoblerow.app.dto;

import lombok.Data;

@Data
public class ConditionDto {
	
	private Integer id;
	
	private String name;
	
	public ConditionDto() {
		
	}
	
	public ConditionDto(Integer id, String name) {
		this.id = id;
		this.name = name;
	}
}

package com.swiatwoblerow.app.dto;

import lombok.Data;

@Data
public class RoleDto {
	
	private Integer id;
	
	private String name;
	
	public RoleDto() {
		
	}
	
	public RoleDto(Integer id, String name) {
		this.id = id;
		this.name = name;
	}
}

package com.swiatwoblerow.app.dto;

import lombok.Data;

@Data
public class RoleDto {
	
	private int id;
	
	private String name;
	
	public RoleDto() {
		
	}
	
	public RoleDto(int id, String name) {
		this.id = id;
		this.name = name;
	}
}

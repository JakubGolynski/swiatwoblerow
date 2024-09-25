package com.swiatwoblerow.app.dto;

public class ConditionDto {
	
	private Integer id;
	
	private String name;
	
	public ConditionDto() {
		
	}
	
	public ConditionDto(Integer id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}

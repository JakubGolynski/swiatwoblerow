package com.swiatwoblerow.app.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class RatingDto {
	
	private Integer id;
	
	private Short value;
	
	private Timestamp createdAt;
	
	private String ownerUsername;

	public RatingDto() {
		
	}
	
	public RatingDto(Integer id, Short value, Timestamp createdAt, String ownerUsername) {
		this.id = id;
		this.value = value;
		this.createdAt = createdAt;
		this.ownerUsername = ownerUsername;
	}
	
}

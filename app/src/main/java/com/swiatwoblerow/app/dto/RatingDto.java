package com.swiatwoblerow.app.dto;

import java.sql.Timestamp;


public class RatingDto {
	
	private Integer id;
	
	private Short value;
	
	private Timestamp createdAt;
	
	private String owner;

	public RatingDto() {
		
	}
	
	public RatingDto(Integer id, Short value, Timestamp createdAt, String owner) {
		this.id = id;
		this.value = value;
		this.createdAt = createdAt;
		this.owner = owner;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Short getValue() {
		return value;
	}

	public void setValue(Short value) {
		this.value = value;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	
	
}

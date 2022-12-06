package com.swiatwoblerow.app.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class ReviewDto {

	private Integer id;
	
	private String message;
	
	private Timestamp createdAt;
	
	private int quantityThumbsUp = 0;
	
	private int quantityThumbsDown = 0;
	
	private String reviewOwner;
	
	public ReviewDto() {
		
	}

	public ReviewDto(Integer id, String message, Timestamp createdAt, int quantityThumbsUp, int quantityThumbsDown,
			String reviewOwner) {
		this.id = id;
		this.message = message;
		this.createdAt = createdAt;
		this.quantityThumbsUp = quantityThumbsUp;
		this.quantityThumbsDown = quantityThumbsDown;
		this.reviewOwner = reviewOwner;
	}
	
}
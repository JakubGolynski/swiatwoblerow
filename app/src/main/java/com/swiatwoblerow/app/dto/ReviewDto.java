package com.swiatwoblerow.app.dto;

import java.sql.Timestamp;


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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public int getQuantityThumbsUp() {
		return quantityThumbsUp;
	}

	public void setQuantityThumbsUp(int quantityThumbsUp) {
		this.quantityThumbsUp = quantityThumbsUp;
	}

	public int getQuantityThumbsDown() {
		return quantityThumbsDown;
	}

	public void setQuantityThumbsDown(int quantityThumbsDown) {
		this.quantityThumbsDown = quantityThumbsDown;
	}

	public String getReviewOwner() {
		return reviewOwner;
	}

	public void setReviewOwner(String reviewOwner) {
		this.reviewOwner = reviewOwner;
	}
	
	
}
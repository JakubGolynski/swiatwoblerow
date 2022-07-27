package com.swiatwoblerow.app.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


import lombok.Data;

@Data
public class ReviewDto {

	private int id;
	
	private String message;
	
	private Timestamp createdAt;
	
	private int quantityThumbsUp = 0;
	
	private int quantityThumbsDown = 0;
	
	private String reviewOwnerUsername;
	
	private List<String> customersWhoLikedReview = new ArrayList<>();
	
	private List<String> customersWhoDislikedReview = new ArrayList<>();
	
	public ReviewDto() {
		
	}

	public ReviewDto(int id, String message, Timestamp createdAt, int quantityThumbsUp, int quantityThumbsDown,
			String reviewOwnerUsername, List<String> customersWhoLikedReview, List<String> customersWhoDislikedReview) {
		this.id = id;
		this.message = message;
		this.createdAt = createdAt;
		this.quantityThumbsUp = quantityThumbsUp;
		this.quantityThumbsDown = quantityThumbsDown;
		this.reviewOwnerUsername = reviewOwnerUsername;
		this.customersWhoLikedReview = customersWhoLikedReview;
		this.customersWhoDislikedReview = customersWhoDislikedReview;
	}
	
}
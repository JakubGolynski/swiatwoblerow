package com.swiatwoblerow.app.dto;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;

@Data
public class ThumbDto {
	
	private Set<String> customersWhoLikedReview = new HashSet<>();
	
	private Set<String> customersWhoDislikedReview = new HashSet<>();
	
	public ThumbDto() {
		
	}

	public ThumbDto(Set<String> customersWhoLikedReview, Set<String> customersWhoDislikedReview) {
		this.customersWhoLikedReview = customersWhoLikedReview;
		this.customersWhoDislikedReview = customersWhoDislikedReview;
	}
}

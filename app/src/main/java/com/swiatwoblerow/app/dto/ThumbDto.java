package com.swiatwoblerow.app.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ThumbDto {
	
	private List<String> customersWhoLikedReview = new ArrayList<>();
	
	private List<String> customersWhoDislikedReview = new ArrayList<>();
	
	public ThumbDto() {
		
	}

	public ThumbDto(List<String> customersWhoLikedReview, List<String> customersWhoDislikedReview) {
		this.customersWhoLikedReview = customersWhoLikedReview;
		this.customersWhoDislikedReview = customersWhoDislikedReview;
	}
}

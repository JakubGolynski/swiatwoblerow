package com.swiatwoblerow.app.service.interfaces;

import java.util.List;

import com.swiatwoblerow.app.dto.ReviewDto;

public interface ReviewService {
//methods to make logic with thumbs up and down
	public ReviewDto addReview(ReviewDto review);
	public List<ReviewDto> getReviews();
}

package com.swiatwoblerow.app.service.interfaces;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.swiatwoblerow.app.dto.ReviewDto;
import com.swiatwoblerow.app.dto.ThumbDto;
import com.swiatwoblerow.app.entity.Review;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;
import com.swiatwoblerow.app.exceptions.TooManyInsertException;

public interface ReviewService {
	public ReviewDto addReview(Integer productId, ReviewDto reviewDto) throws UsernameNotFoundException,NotFoundExceptionRequest,TooManyInsertException;
	public List<ReviewDto> getReviews(Integer productId) throws NotFoundExceptionRequest;
	public void deleteReview(Integer productId) throws NotFoundExceptionRequest, NullPointerException;
	public void addThumbUp(Integer reviewId) throws UsernameNotFoundException,NotFoundExceptionRequest;
	public void deleteThumbUp(Integer reviewId, Integer thumbUpId) throws UsernameNotFoundException,NotFoundExceptionRequest, NullPointerException;
	public void addThumbDown(Integer reviewId) throws UsernameNotFoundException,NotFoundExceptionRequest;
	public void deleteThumbDown(Integer reviewId, Integer thumbDownId) throws UsernameNotFoundException,NotFoundExceptionRequest, NullPointerException;
	public ThumbDto getReviewThumbs(Integer reviewId) throws NotFoundExceptionRequest;
	public boolean hasCustomerAlreadyAddedReview(List<Review> reviewList, String customerUsername);
}

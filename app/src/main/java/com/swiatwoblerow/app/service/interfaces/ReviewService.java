package com.swiatwoblerow.app.service.interfaces;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.swiatwoblerow.app.dto.ReviewDto;
import com.swiatwoblerow.app.dto.ThumbDto;
import com.swiatwoblerow.app.exceptions.CustomerIsNotOwnerException;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;
import com.swiatwoblerow.app.exceptions.TooManyInsertException;
import com.swiatwoblerow.app.service.filter.ReviewFilter;

public interface ReviewService {
	public ReviewDto addReview(Integer productId, ReviewDto reviewDto) throws UsernameNotFoundException,NotFoundExceptionRequest,TooManyInsertException;
	public List<ReviewDto> getReviews(Integer productId, ReviewFilter reviewFilter) throws NotFoundExceptionRequest;
	public void deleteReview(Integer productId) throws NotFoundExceptionRequest, NullPointerException,CustomerIsNotOwnerException;
	public void addThumbUp(Integer reviewId) throws UsernameNotFoundException,NotFoundExceptionRequest,TooManyInsertException;
	public void deleteThumbUp(Integer reviewId) throws UsernameNotFoundException,NotFoundExceptionRequest, NullPointerException,CustomerIsNotOwnerException;
	public void addThumbDown(Integer reviewId) throws UsernameNotFoundException,NotFoundExceptionRequest,TooManyInsertException;
	public void deleteThumbDown(Integer reviewId) throws UsernameNotFoundException,NotFoundExceptionRequest, NullPointerException,CustomerIsNotOwnerException;
	public ThumbDto getReviewThumbs(Integer reviewId) throws NotFoundExceptionRequest;
}

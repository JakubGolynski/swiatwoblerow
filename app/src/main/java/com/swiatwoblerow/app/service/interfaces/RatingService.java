package com.swiatwoblerow.app.service.interfaces;

import java.util.List;

import com.swiatwoblerow.app.dto.RatingDto;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;

public interface RatingService {
	public RatingDto addRating(Integer productId, RatingDto ratingDto) throws NotFoundExceptionRequest;
	public void deleteRating(Integer ratingId) throws NotFoundExceptionRequest, NullPointerException;
	public List<RatingDto> getRatings(Integer productId) throws NotFoundExceptionRequest;
}

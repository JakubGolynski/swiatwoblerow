package com.swiatwoblerow.app.service.interfaces;

import java.util.List;

import com.swiatwoblerow.app.dto.RatingDto;
import com.swiatwoblerow.app.exceptions.CustomerIsNotOwnerException;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;
import com.swiatwoblerow.app.exceptions.TooManyInsertException;
import com.swiatwoblerow.app.service.filter.RatingFilter;

public interface RatingService {
	public RatingDto addRating(Integer productId, RatingDto ratingDto) throws NotFoundExceptionRequest,TooManyInsertException;
	public void deleteRating(Integer ratingId) throws NotFoundExceptionRequest, NullPointerException,CustomerIsNotOwnerException;
	public List<RatingDto> getRatings(Integer productId, RatingFilter ratingFilter) throws NotFoundExceptionRequest;
}

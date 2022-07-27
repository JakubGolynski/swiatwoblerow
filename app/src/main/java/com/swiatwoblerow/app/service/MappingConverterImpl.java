package com.swiatwoblerow.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.swiatwoblerow.app.dto.ReviewDto;
import com.swiatwoblerow.app.entity.Condition;
import com.swiatwoblerow.app.entity.Customer;
import com.swiatwoblerow.app.entity.Review;
import com.swiatwoblerow.app.service.interfaces.MappingConverter;

@Component
public class MappingConverterImpl implements MappingConverter{
	
	@Override
	public List<String> convertCustomersToTheirUsernames(List<Customer> listOfCustomers) {
		return listOfCustomers.stream().map(
			customer -> customer.getUsername()).collect(Collectors.toList());
	}

	@Override
	public List<ReviewDto> convertReviewsToReviewDtos(List<Review> listOfReviews) {
		return listOfReviews.stream().map(
			review -> new ReviewDto(review.getId(),review.getMessage(),
					review.getCreatedAt(),review.getQuantityThumbsUp(),
					review.getQuantityThumbsDown(),review.getReviewOwner().getUsername(),
					this.convertCustomersToTheirUsernames(review.getCustomersWhoLikedReview()),
					this.convertCustomersToTheirUsernames(review.getCustomersWhoDislikedReview())))
					.collect(Collectors.toList());
	}

	@Override
	public List<String> convertConditionsToTheirNames(List<Condition> listOfConditions) {
		return listOfConditions.stream().map(
			condition -> condition.getName()).collect(Collectors.toList());
	}
	
}

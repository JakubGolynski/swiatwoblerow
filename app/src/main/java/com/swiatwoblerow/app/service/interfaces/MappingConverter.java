package com.swiatwoblerow.app.service.interfaces;

import java.util.List;

import com.swiatwoblerow.app.dto.ReviewDto;
import com.swiatwoblerow.app.entity.Condition;
import com.swiatwoblerow.app.entity.Customer;
import com.swiatwoblerow.app.entity.Review;

public interface MappingConverter {
	public List<String> convertCustomersToTheirUsernames(List<Customer> listOfCustomers);
	public List<ReviewDto> convertReviewsToReviewDtos(List<Review> listOfReviews);
	public List<String> convertConditionsToTheirNames(List<Condition> listOfConditions);
}

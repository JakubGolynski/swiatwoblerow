package com.swiatwoblerow.app.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.swiatwoblerow.app.dto.AddressDto;
import com.swiatwoblerow.app.dto.ProductDto;
import com.swiatwoblerow.app.dto.ReviewDto;
import com.swiatwoblerow.app.entity.Address;
import com.swiatwoblerow.app.entity.Condition;
import com.swiatwoblerow.app.entity.Customer;
import com.swiatwoblerow.app.entity.Product;
import com.swiatwoblerow.app.entity.Review;
import com.swiatwoblerow.app.entity.Role;
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

	@Override
	public AddressDto convertAddressToAddressDto(Address address) {
		return new AddressDto(address.getId(),
				address.getCity(),address.getStreet(),address.getHouseNumber(),
				address.getCountry().getName());
	}

	@Override
	public List<String> convertRolesToTheirNames(Set<Role> listOfRoles) {
		return listOfRoles.stream().map(
			role -> role.getName()).collect(Collectors.toList());
	}

}

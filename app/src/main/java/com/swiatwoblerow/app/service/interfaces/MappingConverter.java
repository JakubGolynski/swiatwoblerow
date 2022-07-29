package com.swiatwoblerow.app.service.interfaces;

import java.util.List;
import java.util.Set;

import com.swiatwoblerow.app.dto.AddressDto;
import com.swiatwoblerow.app.dto.ReviewDto;
import com.swiatwoblerow.app.entity.Address;
import com.swiatwoblerow.app.entity.Condition;
import com.swiatwoblerow.app.entity.Customer;
import com.swiatwoblerow.app.entity.Review;
import com.swiatwoblerow.app.entity.Role;

public interface MappingConverter {
	public List<String> convertCustomersToTheirUsernames(List<Customer> listOfCustomers);
	public List<ReviewDto> convertReviewsToReviewDtos(List<Review> listOfReviews);
	public List<String> convertConditionsToTheirNames(List<Condition> listOfConditions);
	public List<String> convertRolesToTheirNames(Set<Role> listOfRoles);
	public AddressDto convertAddressToAddressDto(Address address);
}

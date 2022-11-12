package com.swiatwoblerow.app.validators;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.swiatwoblerow.app.entity.Customer;
import com.swiatwoblerow.app.exceptions.AlreadyExistsException;
import com.swiatwoblerow.app.exceptions.TooManyInsertException;
import com.swiatwoblerow.app.validators.interfaces.ReviewValidator;

@Component
public class ReviewValidatorImpl implements ReviewValidator {

	@Override
	public boolean validateCustomerAlreadyAddedThumbUp(int reviewId, Set<Customer> customersWhoMaybeLikedReview)
			throws TooManyInsertException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validateDeleteThumbUp(int reviewId, Set<Customer> customersWhoMaybeLikedReview)
			throws AlreadyExistsException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validateAddThumbDown(int reviewId) throws AlreadyExistsException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validateDeleteThumbDown(int reviewId) throws AlreadyExistsException {
		// TODO Auto-generated method stub
		return false;
	}

	
}

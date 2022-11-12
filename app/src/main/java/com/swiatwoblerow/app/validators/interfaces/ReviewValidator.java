package com.swiatwoblerow.app.validators.interfaces;

import java.util.Set;

import com.swiatwoblerow.app.entity.Customer;
import com.swiatwoblerow.app.exceptions.AlreadyExistsException;
import com.swiatwoblerow.app.exceptions.TooManyInsertException;

public interface ReviewValidator {
//	public void validateAddReview(revie) throws AlreadyExistsException;
//	public void validateDeleteReview(int reviewId) throws AlreadyExistsException;
	public boolean validateCustomerAlreadyAddedThumbUp(int reviewId, Set<Customer> customersWhoMaybeLikedReview) throws TooManyInsertException;
	public boolean validateDeleteThumbUp(int reviewId, Set<Customer> customersWhoMaybeLikedReview) throws AlreadyExistsException;
	public boolean validateAddThumbDown(int reviewId) throws AlreadyExistsException;
	public boolean validateDeleteThumbDown(int reviewId) throws AlreadyExistsException;
}

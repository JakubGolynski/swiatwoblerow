package com.swiatwoblerow.app.validators.interfaces;

import com.swiatwoblerow.app.dto.CustomerDto;
import com.swiatwoblerow.app.exceptions.AlreadyExistsException;

public interface Validator {
	public void validateCustomer(CustomerDto customerDto) throws AlreadyExistsException;
}

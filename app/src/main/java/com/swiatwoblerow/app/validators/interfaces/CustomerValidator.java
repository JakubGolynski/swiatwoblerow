package com.swiatwoblerow.app.validators.interfaces;

import com.swiatwoblerow.app.dto.CustomerDto;
import com.swiatwoblerow.app.exceptions.AlreadyExistsException;

public interface CustomerValidator {
	public void validate(CustomerDto customerDto) throws AlreadyExistsException;
}

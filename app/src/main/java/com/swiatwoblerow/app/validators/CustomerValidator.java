package com.swiatwoblerow.app.validators;

import org.springframework.stereotype.Component;

import com.swiatwoblerow.app.dto.CustomerDto;
import com.swiatwoblerow.app.exceptions.AlreadyExistsException;
import com.swiatwoblerow.app.repository.CustomerRepository;
import com.swiatwoblerow.app.validators.interfaces.Validator;

@Component
public class CustomerValidator implements Validator{
	
	private CustomerRepository customerRepository;

	public CustomerValidator(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}
	
	@Override
	public void validateCustomer(CustomerDto customerDto) throws AlreadyExistsException{
		validateUsername(customerDto.getUsername());
		validateEmail(customerDto.getEmail());
	}
	
	private void validateUsername(String username) throws AlreadyExistsException{
		boolean isFoundByUsername = customerRepository
				.existsByUsername(username);
		if(isFoundByUsername == true) {
			throw new AlreadyExistsException("Username "+username+" is already in use");
		}
	}

	private void validateEmail(String email) throws AlreadyExistsException{
		boolean isFoundByEmail = customerRepository
				.existsByEmail(email);
		if(isFoundByEmail == true) {
			throw new AlreadyExistsException("Email "+email+" is already in use");
		}
	}

}

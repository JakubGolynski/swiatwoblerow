package com.swiatwoblerow.app.unitests.servicetests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.swiatwoblerow.app.dto.CustomerDto;
import com.swiatwoblerow.app.exceptions.AlreadyExistsException;
import com.swiatwoblerow.app.repository.CustomerRepository;
import com.swiatwoblerow.app.validators.CustomerValidator;

@ExtendWith(MockitoExtension.class)
public class CustomerValidatorTest {
	@Mock
	private CustomerRepository customerRepository;
	private CustomerValidator customerValidator;
	
	@BeforeEach
	void setUp() {
		customerValidator = new CustomerValidator(customerRepository);
	}
	
	@Test
	public void validateCustomerSuccess() throws Exception{
		CustomerDto customerDto = new CustomerDto();
		String customerUsername = "test!@#łUsername";
		String customerPassword = "test1#@łPassword";
		String customerEmail = "test1#@łEmail";
		customerDto.setUsername(customerUsername);
		customerDto.setPassword(customerPassword);
		customerDto.setEmail(customerEmail);
		customerDto.setTelephone("+48512806005");
		
		assertDoesNotThrow(() -> customerValidator.validateCustomer(customerDto));
	}
	
	@Test
	public void validateCustomerFailUsernameAlreadyInUse() {
		CustomerDto customerDto = new CustomerDto();
		String customerUsername = "test!@#łUsername";
		String customerPassword = "test1#@łPassword";
		String customerEmail = "test1#@łEmail";
		customerDto.setUsername(customerUsername);
		customerDto.setPassword(customerPassword);
		customerDto.setEmail(customerEmail);
		customerDto.setTelephone("+48512806005");
		
		when(customerRepository.existsByUsername(customerUsername)).thenReturn(true);
		
		AlreadyExistsException exception = assertThrows(AlreadyExistsException.class,
				() -> {
					customerValidator.validateCustomer(customerDto);
				});
		assertThat(exception.getMessage()).isEqualTo("Username "+
				customerUsername+" is already in use");
		assertThat(exception.getStatus()).isEqualTo(HttpStatus.CONFLICT);
	}
	
	@Test
	public void validateCustomerFailEmailAlreadyInUse() {
		CustomerDto customerDto = new CustomerDto();
		String customerUsername = "test!@#łUsername";
		String customerPassword = "test1#@łPassword";
		String customerEmail = "test1#@łEmail";
		customerDto.setUsername(customerUsername);
		customerDto.setPassword(customerPassword);
		customerDto.setEmail(customerEmail);
		customerDto.setTelephone("+48512806005");
		
		when(customerRepository.existsByEmail(customerEmail)).thenReturn(true);
		
		AlreadyExistsException exception = assertThrows(AlreadyExistsException.class,
				() -> {
					customerValidator.validateCustomer(customerDto);
				});
		assertThat(exception.getMessage()).isEqualTo("Email "+
				customerEmail+" is already in use");
		assertThat(exception.getStatus()).isEqualTo(HttpStatus.CONFLICT);
	}
}

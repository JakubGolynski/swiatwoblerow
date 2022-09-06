package com.swiatwoblerow.app.service.interfaces;


import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.swiatwoblerow.app.dto.CustomerDto;
import com.swiatwoblerow.app.exceptions.AlreadyExistsException;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;

public interface CustomerService {
	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
	public CustomerDto login(CustomerDto customerDto) throws BadCredentialsException,UsernameNotFoundException;
	public List<CustomerDto> getCustomers();
	public CustomerDto getCustomer(Integer id) throws UsernameNotFoundException;
	public CustomerDto addCustomer(CustomerDto customerDto) throws NotFoundExceptionRequest, AlreadyExistsException;
}

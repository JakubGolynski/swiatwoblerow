package com.swiatwoblerow.app.service;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.swiatwoblerow.app.config.jwt.JwtUtils;
import com.swiatwoblerow.app.dto.CustomerDto;
import com.swiatwoblerow.app.entity.Customer;
import com.swiatwoblerow.app.repository.CustomerRepository;
import com.swiatwoblerow.app.service.interfaces.AuthenticationService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
	
	private AuthenticationManager authenticationManager;
	
	private CustomerRepository customerRepository;
	
	private JwtUtils jwtUtils;
	
	private ModelMapper modelMapper;
	
	public AuthenticationServiceImpl(AuthenticationManager authenticationManager, CustomerRepository customerRepository,
			JwtUtils jwtUtils, ModelMapper modelMapper) {
		this.authenticationManager = authenticationManager;
		this.customerRepository = customerRepository;
		this.jwtUtils = jwtUtils;
		this.modelMapper = modelMapper;
	}

	@Override
	public CustomerDto login(CustomerDto customerDto) throws BadCredentialsException, UsernameNotFoundException {
		UsernamePasswordAuthenticationToken tokenAuthentication =
				new UsernamePasswordAuthenticationToken(customerDto.getUsername(),customerDto.getPassword());
		Authentication authentication = authenticationManager.authenticate(tokenAuthentication);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		CustomerPrincipal customerPrincipal = (CustomerPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Customer customer = customerRepository.findByUsername(customerPrincipal.getUsername()).orElseThrow(
				() -> new UsernameNotFoundException("User not found with username "+ customerPrincipal.getUsername()));
		CustomerDto returnCustomerDto = modelMapper.map(customer, CustomerDto.class);
		returnCustomerDto.setJwtToken(jwtUtils.generateJwtToken(customerPrincipal.getUsername(),customer.getRole().getName()));
		return returnCustomerDto;
	}

}

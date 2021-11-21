package com.swiatwoblerow.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.swiatwoblerow.app.repository.CustomerRepository;

@Repository
public class CustomerService{
	
	@Autowired
	private CustomerRepository customerRepository;
	
	
}

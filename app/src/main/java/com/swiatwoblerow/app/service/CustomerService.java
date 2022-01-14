package com.swiatwoblerow.app.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.swiatwoblerow.app.entity.Customer;

public interface CustomerService {
	public List<Customer> findAll();
	public Customer findById(int id);
	public void save(Customer customer);
	public void delete(Customer customer);
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException;
	
}

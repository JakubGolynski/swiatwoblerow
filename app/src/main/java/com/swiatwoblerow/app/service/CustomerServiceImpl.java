package com.swiatwoblerow.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.swiatwoblerow.app.entity.Customer;
import com.swiatwoblerow.app.repository.CustomerRepository;

@Service("customerServiceImpl")
public class CustomerServiceImpl implements CustomerService, UserDetailsService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String username)
											throws UsernameNotFoundException {
		Customer customer = customerRepository.findByUsername(username);
		List<GrantedAuthority> authorities =
							new ArrayList<>();	
		return new CustomerPrincipal(customer,authorities);
	}

	@Override
	public List<Customer> findAll() {
		return customerRepository.findAll();
	}
	
	@Override
	public Customer findById(int id) {
		Optional<Customer> result = customerRepository.findById(id);
		
		Customer theCustomer = null;
		
		if(result.isPresent()) {
			theCustomer = result.get();
		}else {
			throw new RuntimeException("Employee not found with id = "+id);
		}
		return theCustomer;
	}

	@Override
	public void save(Customer customer) {
		String encodedPassword = passwordEncoder.encode(customer.getPassword());
		customer.setPassword(encodedPassword);
		customerRepository.save(customer);
	}

	@Override
	public void delete(Customer customer) {
		customerRepository.save(customer);
	}

}

package com.swiatwoblerow.app.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.swiatwoblerow.app.config.jwt.JwtUtils;
import com.swiatwoblerow.app.dto.CustomerDto;
import com.swiatwoblerow.app.entity.Customer;
import com.swiatwoblerow.app.repository.CustomerRepository;
import com.swiatwoblerow.app.service.interfaces.CustomerService;

@Service("customerServiceImpl")
public class CustomerServiceImpl implements CustomerService, UserDetailsService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
//	@Autowired
//	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Override
	public UserDetails loadUserByUsername(String username)
											throws UsernameNotFoundException {
		Customer customer = customerRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User"+
				" not found with username: "+ username));
		
		List<GrantedAuthority> authorities =
			customer.getRoles().stream()
			.map(role -> new SimpleGrantedAuthority(role.getName()))
			.collect(Collectors.toList());
												
		return new CustomerPrincipal(customer.getUsername(),
				customer.getPassword(),authorities);
	}
	
	@Override
	public CustomerDto login() {
		CustomerDto customer = new CustomerDto();
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		customer.setUsername(username);
		customer.setTokenType("Bearer");
		customer.setAccessToken(jwtUtils.generateJwtToken(username));
		List<String> roles = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
				.stream().map(role -> role.getAuthority()).collect(Collectors.toList());
		customer.setRoles(roles);
		return customer;
	}
	
	@Override
	public CustomerDto getCurrentLoggedInCustomer() {
		CustomerDto customer = new CustomerDto();
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		customer.setUsername(username);
		customer.setTokenType("Bearer");
		customer.setAccessToken(jwtUtils.generateJwtToken(username));
		List<String> roles = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
				.stream().map(role -> role.getAuthority()).collect(Collectors.toList());
		customer.setRoles(roles);
		return customer; 
	}
}

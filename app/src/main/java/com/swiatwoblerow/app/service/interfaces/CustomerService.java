package com.swiatwoblerow.app.service.interfaces;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.swiatwoblerow.app.dto.CustomerDto;

public interface CustomerService {
	UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException;
	public CustomerDto login();
	public CustomerDto getCurrentLoggedInCustomer();
}

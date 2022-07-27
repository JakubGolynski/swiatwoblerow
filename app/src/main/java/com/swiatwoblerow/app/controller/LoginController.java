package com.swiatwoblerow.app.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.swiatwoblerow.app.dto.CustomerDto;
import com.swiatwoblerow.app.service.CustomerServiceImpl;

@RestController
public class LoginController {
	
	@Autowired
	private CustomerServiceImpl customerService;
	
	@PostMapping("/login")
	public CustomerDto login(@RequestBody CustomerDto customerDto){
		return customerService.login(customerDto);
	}
	
}

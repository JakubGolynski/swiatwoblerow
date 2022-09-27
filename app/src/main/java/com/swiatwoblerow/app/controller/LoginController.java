package com.swiatwoblerow.app.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.swiatwoblerow.app.dto.CustomerDto;
import com.swiatwoblerow.app.service.interfaces.AuthenticationService;

@RestController
public class LoginController {
	
	private AuthenticationService authenticationService;
	
	public LoginController(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@PostMapping("/login")
	public CustomerDto login(@RequestBody CustomerDto customerDto){
		return authenticationService.login(customerDto);
	}
	
}

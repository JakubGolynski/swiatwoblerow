package com.swiatwoblerow.app.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swiatwoblerow.app.dto.CustomerDto;
import com.swiatwoblerow.app.service.CustomerServiceImpl;

@RestController
@RequestMapping
public class LoginController {
	
	@Autowired
	private CustomerServiceImpl customerService;
	
	@PostMapping("/login")
	public CustomerDto login(){
		return customerService.login();
//		{
//		       "access_token":"2YotnFZFEjr1zCsicMWpAA",
//		       "token_type":"Bearer"
//		     }
	}
	
//	@GetMapping("/login")
//	public String loginForm(){
//		return "GGG";
//	}
	
	@GetMapping("/secured")
	public String all() {
		return "JEST";
	}
}

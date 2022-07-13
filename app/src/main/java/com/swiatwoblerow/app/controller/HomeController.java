package com.swiatwoblerow.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swiatwoblerow.app.dto.CustomerDto;
import com.swiatwoblerow.app.service.CategoryServiceImpl;
import com.swiatwoblerow.app.service.CustomerServiceImpl;

@RestController
@RequestMapping("api/home")
public class HomeController {
	
	@Autowired
	private CategoryServiceImpl categoryService;
	
	@Autowired
	private CustomerServiceImpl customerService;
	
	@GetMapping("")
	public List<String> findAll() {
		return categoryService.findAll();
	}
	
	@GetMapping("/me")
	public CustomerDto showCustomerData() {
		return customerService.getCurrentLoggedInCustomer();
	}
	
	@PostMapping("/add")
	public String addCategory() {
		return null;
	}
	
}

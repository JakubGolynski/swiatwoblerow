package com.swiatwoblerow.app.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swiatwoblerow.app.dto.CustomerDto;
import com.swiatwoblerow.app.service.CustomerServiceImpl;

@RestController
@RequestMapping("/customers")
public class CustomerController {
	
	private CustomerServiceImpl customerService;
	
	public CustomerController(CustomerServiceImpl customerService) {
		this.customerService = customerService;
	}

	@GetMapping
	public List<CustomerDto> getCustomers(){
		return customerService.getCustomers();
	}
	
	@GetMapping("/{id}")
	public CustomerDto getCustomer(@PathVariable int id) {
		return customerService.getCustomer(id);
	}
	
	@PostMapping
	public CustomerDto addCustomer(@RequestBody CustomerDto customerDto) {
		return customerService.addCustomer(customerDto);
	}
}

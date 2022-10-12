package com.swiatwoblerow.app.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swiatwoblerow.app.dto.CustomerDto;
import com.swiatwoblerow.app.exceptions.AlreadyExistsException;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;
import com.swiatwoblerow.app.service.filter.CustomerFilter;
import com.swiatwoblerow.app.service.interfaces.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerController {
	
	private CustomerService customerService;
	
	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}

	@GetMapping
	public List<CustomerDto> getCustomers(CustomerFilter customerFilter){
		return customerService.getCustomers(customerFilter);
	}
	
	@GetMapping("/{id}")
	public CustomerDto getCustomer(@PathVariable int id) {
		return customerService.getCustomer(id);
	}
	
	@PostMapping
	public CustomerDto addCustomer(@RequestBody CustomerDto customerDto) throws NotFoundExceptionRequest,AlreadyExistsException {
		return customerService.addCustomer(customerDto);
	}
}

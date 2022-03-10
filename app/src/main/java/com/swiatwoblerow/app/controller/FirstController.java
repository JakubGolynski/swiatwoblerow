package com.swiatwoblerow.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.swiatwoblerow.app.entity.Customer;
import com.swiatwoblerow.app.service.interfaces.CustomerService;

@Controller
public class FirstController {
	
	@RequestMapping("/home")
	public String mainPage() {
		return "index";
	}
	
	@GetMapping("/addCustomerForm")
	public String addCustomerForm(Model theModel) {
		theModel.addAttribute("customer", new Customer());
		return "add-customer-form";
	}
	
	@PostMapping("/addCustomerForm")
	public String addCustomerSubmit(@ModelAttribute("customer")Customer customer) {
		return "add-customer-form-result";
	}
	
	@GetMapping("/myAccount")
	public String myAccountSettings(Model theModel) {
		return "my-account-settings";
	}
	
}

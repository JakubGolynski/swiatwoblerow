package com.swiatwoblerow.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.swiatwoblerow.app.dto.ProductDetailsDto;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;
import com.swiatwoblerow.app.service.OfferServiceImpl;

@RestController
@RequestMapping("api/offer")
public class OfferController {
	
	@Autowired
	private OfferServiceImpl offerService;
	
	@GetMapping("/")
	public ProductDetailsDto findById(@RequestParam(name="label")Integer id) 
												throws NotFoundExceptionRequest{
		return offerService.findById(id);
	}
	
	@PostMapping("/add")
	public ProductDetailsDto addOffer(@RequestBody ProductDetailsDto productDetailsDto) {
		return offerService.save(productDetailsDto);
	}
}

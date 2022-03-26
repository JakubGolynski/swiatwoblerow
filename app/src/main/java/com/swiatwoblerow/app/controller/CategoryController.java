package com.swiatwoblerow.app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.swiatwoblerow.app.dto.ProductDto;
import com.swiatwoblerow.app.service.OfferServiceImpl;

@RestController
@RequestMapping("api/category")
public class CategoryController {
	
	@Autowired
	private OfferServiceImpl offerService;
	
	@GetMapping("/cars")
	public List<ProductDto> findAll(@RequestParam Map<String,String> params) {
		return offerService.findAll(params);
	}
	
}

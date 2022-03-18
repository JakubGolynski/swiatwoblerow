package com.swiatwoblerow.app.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swiatwoblerow.app.dto.ProductDto;

@RestController
@RequestMapping("api/category")
public class CategoryController {
	
	@GetMapping("/cars")
	public List<ProductDto> getAllCars() {
		return new ArrayList<>();
	}
	
	@GetMapping("/cars/{id}")
	public ProductDto getCarById(int id){
		return new ProductDto();
	}
	
	@GetMapping("/smartphones")
	public ProductDto getSmartphoneById(int id){
		return new ProductDto();
	}
}

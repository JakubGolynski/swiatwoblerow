package com.swiatwoblerow.app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.swiatwoblerow.app.dto.ProductDto;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;
import com.swiatwoblerow.app.service.filter.ProductFilter;
import com.swiatwoblerow.app.service.interfaces.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {
	
	private ProductService productService;
	
	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping
	public List<ProductDto> getProducts(ProductFilter productFilter) throws NotFoundExceptionRequest{
		return productService.getProducts(productFilter);
	}
	
	@GetMapping("/{id}")
	public ProductDto getProduct(@PathVariable int id) throws NotFoundExceptionRequest{
		return productService.getProduct(id);
	}
	
	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public ProductDto addProduct(@RequestBody ProductDto productDto) throws NotFoundExceptionRequest{
		return productService.addProduct(productDto);
	}
	
}

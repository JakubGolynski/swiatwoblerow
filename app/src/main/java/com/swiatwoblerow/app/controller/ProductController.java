package com.swiatwoblerow.app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.swiatwoblerow.app.dto.ProductDto;
import com.swiatwoblerow.app.dto.RatingDto;
import com.swiatwoblerow.app.exceptions.CustomerIsNotOwnerException;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;
import com.swiatwoblerow.app.exceptions.TooManyInsertException;
import com.swiatwoblerow.app.service.filter.ProductFilter;
import com.swiatwoblerow.app.service.interfaces.ProductService;
import com.swiatwoblerow.app.service.interfaces.RatingService;

@RestController
@RequestMapping("/products")
public class ProductController {
	
	private ProductService productService;
	
	private RatingService ratingService;
	
	public ProductController(ProductService productService, RatingService ratingService) {
		this.productService = productService;
		this.ratingService = ratingService;
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
	
	@PostMapping("/products/{id}/ratings")
	@ResponseStatus(value = HttpStatus.CREATED)
	public RatingDto addRating(@PathVariable int id,@RequestBody RatingDto ratingDto) throws NotFoundExceptionRequest,TooManyInsertException{
		return ratingService.addRating(id,ratingDto);
	}
	
	@DeleteMapping("/products/{productId}/ratings/{ratingId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteRating(@PathVariable int ratingId) throws NotFoundExceptionRequest,CustomerIsNotOwnerException{
		ratingService.deleteRating(ratingId);
	}
	
	@GetMapping("/products/{id}/ratings")
	public List<RatingDto> getRatings(@PathVariable int id) throws NotFoundExceptionRequest{
		return ratingService.getRatings(id);
	}
	
}

package com.swiatwoblerow.app.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ProductDto {
	
	private int id;
	
	private String name;
	
	private Double price;
	
	private Timestamp createdAt;
	
	private Integer quantity;
	
	private String message;
	
	private Double rating;
	
	private CustomerDto productOwner;
	
	private String category;
	
	private List<ReviewDto> reviews = new ArrayList<>();
	
	private List<String> productConditions = new ArrayList<>();

	public ProductDto() {
		
	}

	public ProductDto(int id, String name, Double price, Timestamp createdAt, Integer quantity, String message,
			Double rating, CustomerDto productOwner, String category, List<ReviewDto> reviews,
			List<String> productConditions) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.createdAt = createdAt;
		this.quantity = quantity;
		this.message = message;
		this.rating = rating;
		this.productOwner = productOwner;
		this.category = category;
		this.reviews = reviews;
		this.productConditions = productConditions;
	}

}

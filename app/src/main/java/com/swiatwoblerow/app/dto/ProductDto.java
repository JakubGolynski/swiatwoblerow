package com.swiatwoblerow.app.dto;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;

@Data
public class ProductDto {
	
	private Integer id;
	
	private String name;
	
	private Double price;
	
	private Timestamp createdAt;
	
	private Integer quantity;
	
	private String message;
	
	private Double rating;
	
	private Integer quantityRatings;
	
	private Integer quantityReviews;
	
	private CustomerDto owner;
	
	private CategoryDto category;
	
	private Set<ConditionDto> conditions = new HashSet<>();
	
	public ProductDto() {
		
	}

	public ProductDto(Integer id, String name, Double price, Timestamp createdAt, Integer quantity, String message,
			Double rating, Integer quantityRatings, Integer quantityReviews, CustomerDto owner, CategoryDto category,
			Set<ConditionDto> conditions) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.createdAt = createdAt;
		this.quantity = quantity;
		this.message = message;
		this.rating = rating;
		this.quantityRatings = quantityRatings;
		this.quantityReviews = quantityReviews;
		this.owner = owner;
		this.category = category;
		this.conditions = conditions;
	}

}

package com.swiatwoblerow.app.dto;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;


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
	
	private String owner;
	
	private CategoryDto category;
	
	private ConditionDto condition;
	
	public ProductDto() {
		
	}

	public ProductDto(Integer id, String name, Double price, Timestamp createdAt, Integer quantity, String message,
			Double rating, Integer quantityRatings, Integer quantityReviews, String owner, CategoryDto category,
			ConditionDto condition) {
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
		this.condition = condition;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public Integer getQuantityRatings() {
		return quantityRatings;
	}

	public void setQuantityRatings(Integer quantityRatings) {
		this.quantityRatings = quantityRatings;
	}

	public Integer getQuantityReviews() {
		return quantityReviews;
	}

	public void setQuantityReviews(Integer quantityReviews) {
		this.quantityReviews = quantityReviews;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public CategoryDto getCategory() {
		return category;
	}

	public void setCategory(CategoryDto category) {
		this.category = category;
	}

	public ConditionDto getCondition() {
		return condition;
	}

	public void setCondition(ConditionDto condition) {
		this.condition = condition;
	}

	
}

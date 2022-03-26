package com.swiatwoblerow.app.dto;

import java.sql.Timestamp;

public class ProductDetailsDto {

	private String name;
	
	private Double price;
	
	private String localization;
	
	private Timestamp createdAt;
	
	private String username;
	
	private Integer quantity;
	
	private String message;
	
	public ProductDetailsDto() {

	}
	
	public ProductDetailsDto(String name, Double price, String localization, Timestamp createdAt, String username,
			Integer quantity, String message) {
		this.name = name;
		this.price = price;
		this.localization = localization;
		this.createdAt = createdAt;
		this.username = username;
		this.quantity = quantity;
		this.message = message;
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

	public String getLocalization() {
		return localization;
	}

	public void setLocalization(String localization) {
		this.localization = localization;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

}

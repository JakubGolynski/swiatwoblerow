package com.swiatwoblerow.app.dto;

import java.sql.Timestamp;

public class ProductDto {
	
	private String name;
	
	private Double price;
	
	private String localization;
	
	private Timestamp createdAt;
	
	private Integer label;

	public ProductDto() {
		
	}

	public ProductDto(String name, Double price, String localization, Timestamp createdAt, Integer label) {
		this.name = name;
		this.price = price;
		this.localization = localization;
		this.createdAt = createdAt;
		this.label = label;
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

	public Integer getLabel() {
		return label;
	}

	public void setLabel(Integer label) {
		this.label = label;
	}
	
}

package com.swiatwoblerow.app.service.filter;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

public class ProductFilter {
	
	private String name;
	
	private Double priceFrom = 0.0;
	
	private Double priceTo = 1000000000.0;

	private Double ratingFrom = 0.0;
	
	private String category;
	
	private String city;
	
	private List<String> conditions = new ArrayList<>();

	private Integer page = 0;
	
	private Integer size = 50;
	
	private String sort = "createdAt";
	
	public ProductFilter() {
		
	}

	public ProductFilter(String name, Double priceFrom, Double priceTo, Double ratingFrom, String category, String city,
			List<String> conditions, Integer page, Integer size) {
		this.name = name;
		this.priceFrom = priceFrom;
		this.priceTo = priceTo;
		this.ratingFrom = ratingFrom;
		this.category = category;
		this.city = city;
		this.conditions = conditions;
		this.page = page;
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPriceFrom() {
		return priceFrom;
	}

	public void setPriceFrom(Double priceFrom) {
		this.priceFrom = priceFrom;
	}

	public Double getPriceTo() {
		return priceTo;
	}

	public void setPriceTo(Double priceTo) {
		this.priceTo = priceTo;
	}

	public Double getRatingFrom() {
		return ratingFrom;
	}

	public void setRatingFrom(Double ratingFrom) {
		this.ratingFrom = ratingFrom;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public List<String> getConditions() {
		return conditions;
	}

	public void setConditions(List<String> conditions) {
		this.conditions = conditions;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	
}

package com.swiatwoblerow.app.service.filter;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
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

}

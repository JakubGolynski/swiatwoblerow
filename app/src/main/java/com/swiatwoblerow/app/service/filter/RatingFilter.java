package com.swiatwoblerow.app.service.filter;

public class RatingFilter {
	
	private Integer page = 0;
	private Integer size = 10;
	
	public RatingFilter() {
		
	}

	public RatingFilter(Integer page, Integer size) {
		super();
		this.page = page;
		this.size = size;
	}
}

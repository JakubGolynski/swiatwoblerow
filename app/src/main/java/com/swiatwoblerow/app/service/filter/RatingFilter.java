package com.swiatwoblerow.app.service.filter;

import lombok.Data;

@Data
public class RatingFilter {
	
	private Integer page = 0;
	private Integer size = 10;
	
	public RatingFilter() {
		
	}

	public RatingFilter(Integer page, Integer size) {
		this.page = page;
		this.size = size;
	}
}

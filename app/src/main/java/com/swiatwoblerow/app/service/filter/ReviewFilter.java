package com.swiatwoblerow.app.service.filter;

import lombok.Data;

@Data
public class ReviewFilter {
	
	private Integer page = 0;
	
	private Integer size = 10;

	public ReviewFilter() {
		
	}
	
	public ReviewFilter(Integer page, Integer size) {
		this.page = page;
		this.size = size;
	}
	
}

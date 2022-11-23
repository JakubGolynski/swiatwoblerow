package com.swiatwoblerow.app.service.filter;

import lombok.Data;

@Data
public class ReviewFilter {
	
	private Integer page = 0;
	
	private Integer size = 5;

	public ReviewFilter(Integer page, Integer size) {
		this.page = page;
		this.size = size;
	}
	
}

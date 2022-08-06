package com.swiatwoblerow.app.service.filter;

import lombok.Data;

@Data
public class PaginationFilter {
	
	private Integer page = 0;
	
	private Integer size = 20;

	public PaginationFilter() {
		
	}
	
	public PaginationFilter(Integer page, Integer size) {
		this.page = page;
		this.size = size;
	}
	
}

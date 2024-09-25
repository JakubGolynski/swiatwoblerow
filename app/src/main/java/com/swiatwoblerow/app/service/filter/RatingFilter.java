package com.swiatwoblerow.app.service.filter;

public class RatingFilter {
	
	private Integer page = 0;
	private Integer size = 10;
	
	public RatingFilter() {
		
	}

	public RatingFilter(Integer page, Integer size) {
		this.page = page;
		this.size = size;
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
	
	
}

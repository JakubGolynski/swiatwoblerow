package com.swiatwoblerow.app.dto;

public class ProductDto {
	
	private String productName;
	
	private String productOwner;
	
	private double price;
	
	private double rating;
	
	private String comment;

	public ProductDto() {
		
	}

	public ProductDto(String productName, String productOwner, double price, double rating, String comment) {
		this.productName = productName;
		this.productOwner = productOwner;
		this.price = price;
		this.rating = rating;
		this.comment = comment;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductOwner() {
		return productOwner;
	}

	public void setProductOwner(String productOwner) {
		this.productOwner = productOwner;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
}

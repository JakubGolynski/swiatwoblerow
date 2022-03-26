package com.swiatwoblerow.app.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="opinion")
public class Opinion {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="customer_id",referencedColumnName="id")
	private Customer customer;
	
	@Column(name="message")
	private String message;
	
	@Column(name="created_at")
	private Timestamp createdAt;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="product_details_id")
	private ProductDetails productDetails;
	
	public Opinion() {
		
	}
	
	public Opinion(Customer customer, String message, Timestamp createdAt, ProductDetails productDetails) {
		this.customer = customer;
		this.message = message;
		this.createdAt = createdAt;
		this.productDetails = productDetails;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public ProductDetails getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(ProductDetails productDetails) {
		this.productDetails = productDetails;
	}
	
}

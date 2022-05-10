package com.swiatwoblerow.app.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="product_details")
public class ProductDetails {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="customer_id")
	private Customer customer;
	
	@Column(name="quantity")
	private Integer quantity;
	
	@Column(name="message")
	private String message;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="product_details_thumbs_up",
				joinColumns = @JoinColumn(name="product_details_id"),
				inverseJoinColumns = @JoinColumn(name="customer_id"))
	private List<Customer> thumbsUp = new ArrayList<>();
	
	@ManyToMany
	@JoinTable(name="product_details_thumbs_down",
				joinColumns = @JoinColumn(name="product_details_id"),
				inverseJoinColumns = @JoinColumn(name="customer_id"))
	private List<Customer> thumbsDown = new ArrayList<>();
	
	@Column(name="quantity_thumbsup")
	private Integer quantityThumbsUp;
	
	@Column(name="quantity_thumbsdown")
	private Integer quantityThumbsDown;
	
	public ProductDetails() {
		
	}

	public ProductDetails(Customer customer, Integer quantity, String message, List<Customer> thumbsUp,
			List<Customer> thumbsDown, Integer quantityThumbsUp, Integer quantityThumbsDown) {
		this.customer = customer;
		this.quantity = quantity;
		this.message = message;
		this.thumbsUp = thumbsUp;
		this.thumbsDown = thumbsDown;
		this.quantityThumbsUp = quantityThumbsUp;
		this.quantityThumbsDown = quantityThumbsDown;
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Customer> getThumbsUp() {
		return thumbsUp;
	}

	public void setThumbsUp(List<Customer> thumbsUp) {
		this.thumbsUp = thumbsUp;
	}

	public List<Customer> getThumbsDown() {
		return thumbsDown;
	}

	public void setThumbsDown(List<Customer> thumbsDown) {
		this.thumbsDown = thumbsDown;
	}

	public Integer getQuantityThumbsUp() {
		return quantityThumbsUp;
	}

	public void setQuantityThumbsUp(Integer quantityThumbsUp) {
		this.quantityThumbsUp = quantityThumbsUp;
	}

	public Integer getQuantityThumbsDown() {
		return quantityThumbsDown;
	}

	public void setQuantityThumbsDown(Integer quantityThumbsDown) {
		this.quantityThumbsDown = quantityThumbsDown;
	}
	
}

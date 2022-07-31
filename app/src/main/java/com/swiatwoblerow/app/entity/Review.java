package com.swiatwoblerow.app.entity;

import java.sql.Timestamp;
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

import lombok.Data;

@Entity
@Table(name="review")
@Data
public class Review {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="review_id")
	private Integer id;
	
	@Column(name="message")
	private String message;
	
	@Column(name="created_at")
	private Timestamp createdAt;
	
	@Column(name="quantity_thumbs_up")
	private int quantityThumbsUp = 0;
	
	@Column(name="quantity_thumbs_down")
	private int quantityThumbsDown = 0;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="customer_id")
	private Customer owner;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="product_id")
	private Product product;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="review_thumbs_up",
				joinColumns = @JoinColumn(name="review_id"),
				inverseJoinColumns = @JoinColumn(name="customer_id"))
	private List<Customer> customersWhoLikedReview = new ArrayList<>();
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="review_thumbs_down",
				joinColumns = @JoinColumn(name="review_id"),
				inverseJoinColumns = @JoinColumn(name="customer_id"))
	private List<Customer> customersWhoDislikedReview = new ArrayList<>();
	
	public Review() {
		
	}

	public Review(String message, Timestamp createdAt, int quantityThumbsUp, int quantityThumbsDown, Customer owner,
			Product product, List<Customer> customersWhoLikedReview, List<Customer> customersWhoDislikedReview) {
		this.message = message;
		this.createdAt = createdAt;
		this.quantityThumbsUp = quantityThumbsUp;
		this.quantityThumbsDown = quantityThumbsDown;
		this.owner = owner;
		this.product = product;
		this.customersWhoLikedReview = customersWhoLikedReview;
		this.customersWhoDislikedReview = customersWhoDislikedReview;
	}

}

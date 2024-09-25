package com.swiatwoblerow.app.entity;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

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
@Table(name="review")
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
	
	@Column(name="review_owner")
	private String reviewOwner;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="customer_id")
	private Customer owner;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="product_id")
	private Product product;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="review_thumbs_up",
				joinColumns = @JoinColumn(name="review_id"),
				inverseJoinColumns = @JoinColumn(name="customer_id"))
	private Set<Customer> customersWhoLikedReview = new HashSet<>();
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="review_thumbs_down",
				joinColumns = @JoinColumn(name="review_id"),
				inverseJoinColumns = @JoinColumn(name="customer_id"))
	private Set<Customer> customersWhoDislikedReview = new HashSet<>();
	
	public Review() {
		
	}

	public Review(Integer id, String message, Timestamp createdAt, int quantityThumbsUp, int quantityThumbsDown,
			String reviewOwner, Customer owner, Product product, Set<Customer> customersWhoLikedReview,
			Set<Customer> customersWhoDislikedReview) {
		this.id = id;
		this.message = message;
		this.createdAt = createdAt;
		this.quantityThumbsUp = quantityThumbsUp;
		this.quantityThumbsDown = quantityThumbsDown;
		this.reviewOwner = reviewOwner;
		this.owner = owner;
		this.product = product;
		this.customersWhoLikedReview = customersWhoLikedReview;
		this.customersWhoDislikedReview = customersWhoDislikedReview;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public int getQuantityThumbsUp() {
		return quantityThumbsUp;
	}

	public void setQuantityThumbsUp(int quantityThumbsUp) {
		this.quantityThumbsUp = quantityThumbsUp;
	}

	public int getQuantityThumbsDown() {
		return quantityThumbsDown;
	}

	public void setQuantityThumbsDown(int quantityThumbsDown) {
		this.quantityThumbsDown = quantityThumbsDown;
	}

	public String getReviewOwner() {
		return reviewOwner;
	}

	public void setReviewOwner(String reviewOwner) {
		this.reviewOwner = reviewOwner;
	}

	public Customer getOwner() {
		return owner;
	}

	public void setOwner(Customer owner) {
		this.owner = owner;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Set<Customer> getCustomersWhoLikedReview() {
		return customersWhoLikedReview;
	}

	public void setCustomersWhoLikedReview(Set<Customer> customersWhoLikedReview) {
		this.customersWhoLikedReview = customersWhoLikedReview;
	}

	public Set<Customer> getCustomersWhoDislikedReview() {
		return customersWhoDislikedReview;
	}

	public void setCustomersWhoDislikedReview(Set<Customer> customersWhoDislikedReview) {
		this.customersWhoDislikedReview = customersWhoDislikedReview;
	}
	
	

}

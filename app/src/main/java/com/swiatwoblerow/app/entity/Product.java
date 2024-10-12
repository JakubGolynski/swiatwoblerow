package com.swiatwoblerow.app.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import org.hibernate.validator.constraints.Length;


@Entity
@Table(name="product")
public class Product {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="product_id")
	private Integer id;
	
	@Length(max=50)
	@Column(name="name")
	private String name;
	
	@Column(name="price")
	private Double price;
	
	@Column(name="created_at")
	private Timestamp createdAt;
	
	@Column(name="quantity")
	private Integer quantity;
	
	@Column(name="message")
	private String message;
	
	@Column(name="rating")
	private Double rating;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="product_condition",
				joinColumns = @JoinColumn(name="product_id"),
				inverseJoinColumns = @JoinColumn(name="condition_id"))
	private Set<Condition> conditions = new HashSet<>();
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="category_id")
	private Category category;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="customer_id")
	private Customer owner;
	
	@Column(name="quantity_reviews")
	private Integer quantityReviews;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy="product")
	private List<Review> reviews = new ArrayList<>();
	
	@Column(name="quantity_ratings")
	private Integer quantityRatings;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy="product")
	private List<Rating> ratings = new ArrayList<>();
	
	public Product() {
		
	}

	public Product(@Length(max = 50) String name, Double price, Timestamp createdAt, Integer quantity, String message,
			Double rating, Set<Condition> conditions, Category category, Customer owner, Integer quantityReviews,
			List<Review> reviews, Integer quantityRatings, List<Rating> ratings) {
		this.name = name;
		this.price = price;
		this.createdAt = createdAt;
		this.quantity = quantity;
		this.message = message;
		this.rating = rating;
		this.conditions = conditions;
		this.category = category;
		this.owner = owner;
		this.quantityReviews = quantityReviews;
		this.reviews = reviews;
		this.quantityRatings = quantityRatings;
		this.ratings = ratings;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
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

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public Set<Condition> getConditions() {
		return conditions;
	}

	public void setConditions(Set<Condition> conditions) {
		this.conditions = conditions;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Customer getOwner() {
		return owner;
	}

	public void setOwner(Customer owner) {
		this.owner = owner;
	}

	public Integer getQuantityReviews() {
		return quantityReviews;
	}

	public void setQuantityReviews(Integer quantityReviews) {
		this.quantityReviews = quantityReviews;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public Integer getQuantityRatings() {
		return quantityRatings;
	}

	public void setQuantityRatings(Integer quantityRatings) {
		this.quantityRatings = quantityRatings;
	}

	public List<Rating> getRatings() {
		return ratings;
	}

	public void setRatings(List<Rating> ratings) {
		this.ratings = ratings;
	}

	
}

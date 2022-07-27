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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Entity
@Table(name="product")
@Data
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
	private List<Condition> productConditions = new ArrayList<>();
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="category_id")
	private Category category;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="customer_id")
	private Customer customer;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy="product")
	private List<Review> reviews = new ArrayList<>();
	
	public Product() {
		
	}

	public Product(@Length(max = 50) String name, Double price, Timestamp createdAt, Integer quantity, String message,
			Double rating, List<Condition> productConditions, Category category, Customer customer,
			List<Review> reviews) {
		this.name = name;
		this.price = price;
		this.createdAt = createdAt;
		this.quantity = quantity;
		this.message = message;
		this.rating = rating;
		this.productConditions = productConditions;
		this.category = category;
		this.customer = customer;
		this.reviews = reviews;
	}

}

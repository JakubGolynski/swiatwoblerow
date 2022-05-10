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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name="product")
public class Product {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Length(max=50)
	@Column(name="name")
	private String name;
	
	@Column(name="price")
	private Double price;
	
	@Column(name="localization")
	private String localization;
	
	@Column(name="created_at")
	private Timestamp createdAt;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="product_details_id",referencedColumnName="id")
	private ProductDetails productDetails;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="product_condition",
				joinColumns = @JoinColumn(name="product_id"),
				inverseJoinColumns = @JoinColumn(name="condition_id"))
	private List<Condition> productCondition = new ArrayList<>();
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="category_id")
	private Category category;
	
	public Product() {
		
	}

	public Product(@Length(max = 50) String name, Double price, String localization, Timestamp createdAt,
			ProductDetails productDetails, List<Condition> productCondition, Category category) {
		this.name = name;
		this.price = price;
		this.localization = localization;
		this.createdAt = createdAt;
		this.productDetails = productDetails;
		this.productCondition = productCondition;
		this.category = category;
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

	public String getLocalization() {
		return localization;
	}

	public void setLocalization(String localization) {
		this.localization = localization;
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

	public List<Condition> getProductCondition() {
		return productCondition;
	}

	public void setProductCondition(List<Condition> productCondition) {
		this.productCondition = productCondition;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
}

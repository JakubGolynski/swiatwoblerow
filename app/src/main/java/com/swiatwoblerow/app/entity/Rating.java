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
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="rating")
@Data
public class Rating {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="rating_id")
	private Integer id;
	
	@Column(name="value")
	private Short value;
	
	@Column(name="created_at")
	private Timestamp createdAt;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="customer_id")
	private Customer owner;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="product_id")
	private Product product;
	
	public Rating() {
		
	}

	public Rating(Short value, Timestamp createdAt, Customer owner, Product product) {
		this.value = value;
		this.createdAt = createdAt;
		this.owner = owner;
		this.product = product;
	}
	
}

package com.swiatwoblerow.app.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="category")
@Data
public class Category {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="category_id")
	private Integer id;
	
	@Column(name="name")
	private String name;
	
	public Category() {
		
	}

	public Category(String name) {
		this.name = name;
	}
	
}

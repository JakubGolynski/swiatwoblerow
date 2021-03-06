package com.swiatwoblerow.app.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="condition")
@Data
public class Condition {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="condition_id")
	private Integer id;
	
	@Column(name="name")
	private String name;

	public Condition() {
		
	}
	
	public Condition(String name) {
		this.name = name;
	}

}

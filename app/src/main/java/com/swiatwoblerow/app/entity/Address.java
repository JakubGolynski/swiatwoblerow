package com.swiatwoblerow.app.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="address")
@Data
public class Address {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="address_id")
	private Integer id;
	
	@Column(name="city")
	private String city;
	
	@Column(name="street")
	private String street;
	
	@Column(name="house_number")
	private String houseNumber;
	
	@ManyToOne
	@JoinColumn(name="country_id")
	private Country country;
	
	public Address() {
		
	}

	public Address(String city, String street, String houseNumber, Country country) {
		this.city = city;
		this.street = street;
		this.houseNumber = houseNumber;
		this.country = country;
	}
	
}

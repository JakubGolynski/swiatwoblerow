package com.swiatwoblerow.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swiatwoblerow.app.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	
}

package com.swiatwoblerow.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.swiatwoblerow.app.entity.Customer;
import com.swiatwoblerow.app.entity.Product;
import com.swiatwoblerow.app.entity.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {
	Optional<Rating> findByOwnerAndProduct(Customer owner, Product product);
}

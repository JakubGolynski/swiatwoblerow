package com.swiatwoblerow.app.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.swiatwoblerow.app.entity.Category;
import com.swiatwoblerow.app.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{
	List<Product> findAllByNameContainingIgnoreCaseAndPriceBetweenAndRatingGreaterThanEqualAndCategory
	(String name, Double priceFrom, Double priceTo, Double ratingFrom,
				Category category,Pageable pageable);
	List<Product> findAllByNameContainingIgnoreCaseAndPriceBetweenAndRatingGreaterThanEqual
	(String name, Double priceFrom, Double priceTo, Double ratingFrom,
			Pageable pageable);
}
package com.swiatwoblerow.app.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.swiatwoblerow.app.entity.Customer;
import com.swiatwoblerow.app.entity.Product;
import com.swiatwoblerow.app.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
	List<Review> findAllByProduct(Product product);
	boolean existsByOwnerAndProduct(Customer owner, Product product);
	boolean existsByIdAndCustomersWhoLikedReviewIn(Integer reviewId,Set<Customer> customersWhoLikedReview);
	boolean existsByIdAndCustomersWhoDislikedReviewIn(Integer reviewId,Set<Customer> customersWhoDislikedReview);
}

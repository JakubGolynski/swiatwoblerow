package com.swiatwoblerow.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.swiatwoblerow.app.entity.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {

}

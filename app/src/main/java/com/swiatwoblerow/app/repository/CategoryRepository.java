package com.swiatwoblerow.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.swiatwoblerow.app.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	Optional<Category> findByName(String name); 
	boolean existsByName(String name);
}

package com.swiatwoblerow.app.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.swiatwoblerow.app.entity.Product;
import com.swiatwoblerow.app.repository.custom.ProductRepositoryCustom;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, ProductRepositoryCustom{
	
	@EntityGraph(attributePaths = { "conditions","category","owner.address.country"})
	List<Product> findByIdIn(List<Integer> productIdList, Sort sortBy);
}
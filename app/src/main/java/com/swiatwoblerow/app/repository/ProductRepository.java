package com.swiatwoblerow.app.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.swiatwoblerow.app.entity.Product;
import com.swiatwoblerow.app.repository.projections.OnlyProductIds;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product>{
	
	@Query("select p.id from Product p")
	Page<Integer> getAllIds(Specification<Product> spec, Pageable pageable);
	//	@Override
//	Page<OnlyProductIds> findAll(Specification<Product> spec, Pageable pageable);
}
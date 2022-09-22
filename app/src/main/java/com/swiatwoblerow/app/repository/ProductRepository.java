package com.swiatwoblerow.app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.swiatwoblerow.app.entity.Product;
import com.swiatwoblerow.app.repository.custom.ProductRepositoryCustom;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, ProductRepositoryCustom{
	Page<Product> findByIdIn(List<Integer> productIdList, Pageable pageable);
}
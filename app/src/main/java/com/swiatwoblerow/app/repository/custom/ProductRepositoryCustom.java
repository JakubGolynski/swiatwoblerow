package com.swiatwoblerow.app.repository.custom;

import java.util.List;

import com.swiatwoblerow.app.entity.Product;
import com.swiatwoblerow.app.service.filter.ProductFilter;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepositoryCustom{
	List<Product> getProductsList(ProductFilter productFilter);
}

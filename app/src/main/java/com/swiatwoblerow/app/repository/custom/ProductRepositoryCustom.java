package com.swiatwoblerow.app.repository.custom;

import java.util.List;

import com.swiatwoblerow.app.service.filter.ProductFilter;

public interface ProductRepositoryCustom {
	List<Integer> getProductIdList(ProductFilter productFilter);
}

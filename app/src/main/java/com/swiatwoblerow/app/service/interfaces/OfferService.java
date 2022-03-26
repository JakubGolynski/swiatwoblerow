package com.swiatwoblerow.app.service.interfaces;

import java.util.List;
import java.util.Map;

import com.swiatwoblerow.app.dto.ProductDetailsDto;
import com.swiatwoblerow.app.dto.ProductDto;

public interface OfferService {
	public ProductDetailsDto save(ProductDetailsDto productDetailsDto);
	public List<ProductDto> findAll(Map<String,String> params);
	public ProductDetailsDto findById(Integer id);
}

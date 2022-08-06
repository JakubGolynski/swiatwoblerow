package com.swiatwoblerow.app.service.interfaces;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.swiatwoblerow.app.dto.ProductDto;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;
import com.swiatwoblerow.app.service.filter.ProductFilter;

public interface ProductService {
	public ProductDto addProduct(ProductDto productDto) throws UsernameNotFoundException,NotFoundExceptionRequest;
	public List<ProductDto> getProducts(ProductFilter productFilter) throws NotFoundExceptionRequest;
	public ProductDto getProduct(Integer id) throws NotFoundExceptionRequest;
}

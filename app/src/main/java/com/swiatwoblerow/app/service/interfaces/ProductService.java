package com.swiatwoblerow.app.service.interfaces;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.swiatwoblerow.app.dto.ProductDto;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;

public interface ProductService {
	public ProductDto addProduct(ProductDto productDto) throws UsernameNotFoundException,NotFoundExceptionRequest;
	public List<ProductDto> getProducts(Map<String,String> params) throws NotFoundExceptionRequest;
	public ProductDto getProduct(Integer id) throws NotFoundExceptionRequest;
}

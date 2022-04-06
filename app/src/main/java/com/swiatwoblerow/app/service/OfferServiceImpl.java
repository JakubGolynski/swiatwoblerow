package com.swiatwoblerow.app.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.swiatwoblerow.app.dto.ProductDetailsDto;
import com.swiatwoblerow.app.dto.ProductDto;
import com.swiatwoblerow.app.entity.Customer;
import com.swiatwoblerow.app.entity.Product;
import com.swiatwoblerow.app.entity.ProductDetails;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;
import com.swiatwoblerow.app.repository.CustomerRepository;
import com.swiatwoblerow.app.repository.ProductDetailsRepository;
import com.swiatwoblerow.app.repository.ProductRepository;
import com.swiatwoblerow.app.service.interfaces.OfferService;

@Service
public class OfferServiceImpl implements OfferService {
	
	@Autowired
	private ProductDetailsRepository productDetailsRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Override
	public List<ProductDto> findAll(Map<String,String> params){
		
		Product productExample = new Product();
		productExample.setName(params.getOrDefault("name", null));
		productExample.setLocalization(params.getOrDefault("localization", null));
		
		Example<Product> example = Example.of(productExample,ExampleMatcher
				.matchingAll()
				.withMatcher("name", ExampleMatcher
						.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING))
				.withMatcher("localization", ExampleMatcher
						.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING))
				.withIgnoreCase());
		
		return productRepository.findAll(example).stream()
		.map(product -> new ProductDto(product.getName(),
				product.getPrice(),product.getLocalization(),
				product.getCreatedAt(),product.getId()))
		.collect(Collectors.toList());
	}
	
	@Override
	public ProductDetailsDto save(ProductDetailsDto productDetailsDto)
									throws UsernameNotFoundException{
		Product product = new Product();
		product.setName(productDetailsDto.getName());
		product.setPrice(productDetailsDto.getPrice());
		product.setLocalization(productDetailsDto.getLocalization());
		product.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		
		ProductDetails productDetails = new ProductDetails();
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Customer customer = customerRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User "
						+ "not found with username: "+username));
				
		productDetails.setCustomer(customer);
		productDetails.setQuantity(productDetailsDto.getQuantity());
		productDetails.setMessage(productDetailsDto.getMessage());
		productDetailsDto.setUsername(username);
		productDetailsDto.setCreatedAt(product.getCreatedAt());
		
		productDetailsRepository.save(productDetails);
		
		product.setProductDetails(productDetails);
		
		productRepository.save(product);
		return productDetailsDto;
	}
	
	@Override 
	public ProductDetailsDto findById(Integer id) 
											throws NotFoundExceptionRequest{
		
		ProductDetailsDto productDetailsDto = new ProductDetailsDto();
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new NotFoundExceptionRequest("Product with label "+
		id.toString()+" not found"));
				
		productDetailsDto.setName(product.getName());
		productDetailsDto.setPrice(product.getPrice());
		productDetailsDto.setLocalization(product.getLocalization());
		productDetailsDto.setCreatedAt(product.getCreatedAt());
		productDetailsDto.setUsername(product.getProductDetails().getCustomer().getUsername());
		productDetailsDto.setQuantity(product.getProductDetails().getQuantity());
		productDetailsDto.setMessage(product.getProductDetails().getMessage());
		
		return productDetailsDto;
	}
}

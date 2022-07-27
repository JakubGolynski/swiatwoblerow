package com.swiatwoblerow.app.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.swiatwoblerow.app.dto.ProductDto;
import com.swiatwoblerow.app.entity.Category;
import com.swiatwoblerow.app.entity.Condition;
import com.swiatwoblerow.app.entity.Customer;
import com.swiatwoblerow.app.entity.Product;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;
import com.swiatwoblerow.app.repository.CategoryRepository;
import com.swiatwoblerow.app.repository.CustomerRepository;
import com.swiatwoblerow.app.repository.ProductRepository;
import com.swiatwoblerow.app.service.interfaces.MappingConverter;
import com.swiatwoblerow.app.service.interfaces.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	
	private MappingConverter mappingConverter;
	
	private CustomerServiceImpl customerService;
	
	private ProductRepository productRepository;
	
	private CustomerRepository customerRepository;
	
	private CategoryRepository categoryRepository;

	public ProductServiceImpl(MappingConverter mappingConverter, CustomerServiceImpl customerService,
			ProductRepository productRepository, CustomerRepository customerRepository,
			CategoryRepository categoryRepository) {
		this.mappingConverter = mappingConverter;
		this.customerService = customerService;
		this.productRepository = productRepository;
		this.customerRepository = customerRepository;
		this.categoryRepository = categoryRepository;
	}

	@Override
	public ProductDto addProduct(ProductDto productDto) throws UsernameNotFoundException,NotFoundExceptionRequest{
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Customer customer = customerRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User "
						+ "not found with username: "+username));
		Product product = new Product();
		product.setName(productDto.getName());
		product.setPrice(productDto.getPrice());
		product.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		product.setQuantity(productDto.getQuantity());
		product.setMessage(productDto.getMessage());
		product.setRating(5.0);
		product.setProductConditions(
				productDto.getProductConditions().stream().map(
						condition -> new Condition(condition)).collect(Collectors.toList()));
		Category category = categoryRepository.findByName(productDto.getCategory())
				.orElseThrow(() -> new NotFoundExceptionRequest("Category with name "+
						productDto.getCategory()+ " not found"));
		product.setCategory(category);
		product.setCustomer(customer);
		
		productRepository.save(product);
		return productDto;
	}

	@Override
	public List<ProductDto> getProducts(Map<String, String> params) throws NotFoundExceptionRequest{
		
		Product productExample = new Product();
		productExample.setName(params.getOrDefault("search", null));
		Example<Product> example = Example.of(productExample,ExampleMatcher
				.matchingAll()
				.withMatcher("name", ExampleMatcher
						.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING))
				.withMatcher("localization", ExampleMatcher
						.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING))
				.withIgnoreCase());
		
		return productRepository.findAll(example).stream()
			.map(product -> new ProductDto(product.getId(),product.getName(),
					product.getPrice(),product.getCreatedAt(),product.getQuantity(),
					product.getMessage(),product.getRating(),
					customerService.getCustomer(product.getCustomer().getId()),
					product.getCategory().getName(),
					mappingConverter.convertReviewsToReviewDtos(product.getReviews()),
					mappingConverter.convertConditionsToTheirNames(product.getProductConditions())))
			.collect(Collectors.toList());
	}

	@Override
	public ProductDto getProduct(Integer id) throws NotFoundExceptionRequest {
		ProductDto productDto = new ProductDto();
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new NotFoundExceptionRequest("Product with id "+
		id+" not found"));
		productDto.setId(id);
		productDto.setName(product.getName());
		productDto.setPrice(product.getPrice());
		productDto.setCreatedAt(product.getCreatedAt());
		productDto.setQuantity(product.getQuantity());
		productDto.setMessage(product.getMessage());
		productDto.setRating(product.getRating());
		productDto.setProductOwner(customerService.getCustomer(product.getCustomer().getId()));
		productDto.setCategory(product.getCategory().getName());
		productDto.setReviews(mappingConverter.convertReviewsToReviewDtos(product.getReviews()));
		productDto.setProductConditions(
				mappingConverter.convertConditionsToTheirNames(product.getProductConditions()));
		return productDto;
	}

}

package com.swiatwoblerow.app.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import com.swiatwoblerow.app.repository.specification.ProductSpecification;
import com.swiatwoblerow.app.service.interfaces.ProductService;
import org.springframework.data.domain.Sort;



@Service
public class ProductServiceImpl implements ProductService {
	
	private CustomerServiceImpl customerService;
	
	private ProductRepository productRepository;
	
	private CustomerRepository customerRepository;
	
	private CategoryRepository categoryRepository;
	
	private ModelMapper modelMapper;

	public ProductServiceImpl(CustomerServiceImpl customerService,
			ProductRepository productRepository, CustomerRepository customerRepository,
			CategoryRepository categoryRepository, ModelMapper modelMapper) {
		this.customerService = customerService;
		this.productRepository = productRepository;
		this.customerRepository = customerRepository;
		this.categoryRepository = categoryRepository;
		this.modelMapper = modelMapper;
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
		product.setConditions(
				productDto.getConditions().stream().map(
						condition -> new Condition(condition)).collect(Collectors.toList()));
		Category category = categoryRepository.findByName(productDto.getCategory().getName()).orElseThrow(
				() -> new NotFoundExceptionRequest("Category with name "+
						productDto.getCategory().getName()+" does not exist"));
		product.setCategory(category);
		product.setOwner(customer);
		productRepository.save(product);
		
		productDto.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		productDto.setRating(5.0);
		productDto.setOwner(customerService.getCustomer(customer.getId()));
		return productDto;
	}

	@Override
	public List<ProductDto> getProducts(Map<String, String> params) throws NotFoundExceptionRequest{
		String name = params.getOrDefault("name", "");
		Double priceFrom = Double.valueOf(params.getOrDefault("price_from", "0"));
		Double priceTo = Double.valueOf(params.getOrDefault("price_to", "1000000000"));
		Double ratingFrom = Double.valueOf(params.getOrDefault("rating_from", "0"));
		String category = params.getOrDefault("category", "");
		String city = params.getOrDefault("city", "");
		
		Pageable pageable = PageRequest.of(Integer.valueOf(params.getOrDefault("page", "0")),
				Integer.valueOf(params.getOrDefault("size", "20")), Sort.by("createdAt").descending());
		
		return productRepository.findAll(ProductSpecification.isNameLike(name)
					.and(ProductSpecification.isPriceBetween(priceFrom,priceTo))
					.and(ProductSpecification.isRatingGreaterThan(ratingFrom))
					.and(ProductSpecification.isCategoryEqual(category))
					.and(ProductSpecification.isCityEqual(city)),pageable).stream()
				.map(product -> modelMapper.map(product, ProductDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public ProductDto getProduct(Integer id) throws NotFoundExceptionRequest {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new NotFoundExceptionRequest("Product with id "+
		id+" not found"));
		ProductDto productDto = modelMapper.map(product, ProductDto.class);
		return productDto;
	}

}

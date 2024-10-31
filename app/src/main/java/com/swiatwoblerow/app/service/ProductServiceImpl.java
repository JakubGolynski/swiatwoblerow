package com.swiatwoblerow.app.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import com.swiatwoblerow.app.dto.ConditionDto;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.swiatwoblerow.app.dto.CategoryDto;
import com.swiatwoblerow.app.dto.ProductDto;
import com.swiatwoblerow.app.entity.Category;
import com.swiatwoblerow.app.entity.Condition;
import com.swiatwoblerow.app.entity.Customer;
import com.swiatwoblerow.app.entity.Product;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;
import com.swiatwoblerow.app.repository.CategoryRepository;
import com.swiatwoblerow.app.repository.ConditionRepository;
import com.swiatwoblerow.app.repository.CustomerRepository;
import com.swiatwoblerow.app.repository.ProductRepository;
import com.swiatwoblerow.app.service.filter.ProductFilter;
import com.swiatwoblerow.app.service.interfaces.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	
	private ProductRepository productRepository;
	
	private CustomerRepository customerRepository;
	
	private CategoryRepository categoryRepository;
	
	private ConditionRepository conditionRepository;
	
	private ModelMapper modelMapper;

	public ProductServiceImpl(ProductRepository productRepository,
			CustomerRepository customerRepository, CategoryRepository categoryRepository,
			ConditionRepository conditionRepository, ModelMapper modelMapper) {
		this.productRepository = productRepository;
		this.customerRepository = customerRepository;
		this.categoryRepository = categoryRepository;
		this.conditionRepository = conditionRepository;
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
		String conditionName = productDto.getCondition().getName();
		Condition condition = conditionRepository.findByName(conditionName);
		product.setCondition(condition);
		Category category = categoryRepository.findByName(productDto.getCategory().getName()).orElseThrow(
				() -> new NotFoundExceptionRequest("Category with name "+
						productDto.getCategory().getName()+" does not exist"));
		product.setCategory(category);
		product.setOwner(customer);
		product.setQuantityReviews(0);
		product.setQuantityRatings(0);
		productRepository.save(product);
		
		return modelMapper.map(product, ProductDto.class);
	}

	@Override
	public List<ProductDto> getProducts(ProductFilter productFilter) throws NotFoundExceptionRequest{

		return productRepository.getProductsList(productFilter).stream()
				.map(product -> new ProductDto(
						product.getId(), product.getName(), product.getPrice(), product.getCreatedAt(), product.getQuantity(),
						product.getMessage(), product.getRating(), product.getQuantityRatings(), product.getQuantityReviews(),
						product.getOwner().getUsername(), new CategoryDto(),
						new ConditionDto(product.getCondition().getId(),product.getCondition().getName())
						))
				.collect(Collectors.toList());
	}

	@Override
	public ProductDto getProduct(Integer id) throws NotFoundExceptionRequest {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new NotFoundExceptionRequest("Product with id "+
		id+" not found"));
		return modelMapper.map(product, ProductDto.class);
	}

}

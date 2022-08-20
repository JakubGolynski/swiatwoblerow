package com.swiatwoblerow.app.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.swiatwoblerow.app.dto.CategoryDto;
import com.swiatwoblerow.app.dto.ConditionDto;
import com.swiatwoblerow.app.dto.CustomerDto;
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
import com.swiatwoblerow.app.repository.specification.ProductSpecification;
import com.swiatwoblerow.app.service.filter.ProductFilter;
import com.swiatwoblerow.app.service.interfaces.ProductService;
import org.springframework.data.domain.Sort;

@Service
public class ProductServiceImpl implements ProductService {
	
	private ProductRepository productRepository;
	
	private CustomerRepository customerRepository;
	
	private CategoryRepository categoryRepository;
	
	private ConditionRepository conditionRepository;
	
	private ModelMapper modelMapper;

	public ProductServiceImpl(ProductRepository productRepository, CustomerRepository customerRepository,
			CategoryRepository categoryRepository, ConditionRepository conditionRepository, ModelMapper modelMapper) {
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
		Set<String> conditionNamesFromDto = productDto.getConditions().stream().map(
				condition -> condition.getName()).collect(Collectors.toSet());
		Set<Condition> conditions = conditionRepository.findByNameIn(conditionNamesFromDto);
		product.setConditions(conditions);
		Category category = categoryRepository.findByName(productDto.getCategory().getName()).orElseThrow(
				() -> new NotFoundExceptionRequest("Category with name "+
						productDto.getCategory().getName()+" does not exist"));
		product.setCategory(category);
		product.setOwner(customer);
		product.setQuantityReviews(0);
		product.setQuantityRatings(0);
		productRepository.save(product);
		
		ProductDto returnProductDto = modelMapper.map(product, ProductDto.class);
		return returnProductDto;
	}

	@Override
	public List<ProductDto> getProducts(ProductFilter productFilter) throws NotFoundExceptionRequest{
		
		Pageable pageable = PageRequest.of(productFilter.getPage(),
				productFilter.getSize(), Sort.by(productFilter.getSort()));
		
		return productRepository.findAll(ProductSpecification.isNameLike(productFilter.getName())
					.and(ProductSpecification.isPriceBetween(productFilter.getPriceFrom(),productFilter.getPriceTo()))
					.and(ProductSpecification.isRatingGreaterThan(productFilter.getRatingFrom()))
					.and(ProductSpecification.isCategoryEqual(productFilter.getCategory()))
					.and(ProductSpecification.isCityEqual(productFilter.getCity())),pageable).stream()
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

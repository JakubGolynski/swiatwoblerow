package com.swiatwoblerow.app.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.swiatwoblerow.app.dto.RatingDto;
import com.swiatwoblerow.app.entity.Customer;
import com.swiatwoblerow.app.entity.Product;
import com.swiatwoblerow.app.entity.Rating;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;
import com.swiatwoblerow.app.repository.CustomerRepository;
import com.swiatwoblerow.app.repository.ProductRepository;
import com.swiatwoblerow.app.repository.RatingRepository;
import com.swiatwoblerow.app.service.interfaces.RatingService;

@Service
public class RatingServiceImpl implements RatingService{
	
	private ProductRepository productRepository;
	
	private RatingRepository ratingRepository;
	
	private CustomerRepository customerRepository;

	private ModelMapper modelMapper;

	public RatingServiceImpl(ProductRepository productRepository, RatingRepository ratingRepository,
			CustomerRepository customerRepository, ModelMapper modelMapper) {
		this.productRepository = productRepository;
		this.ratingRepository = ratingRepository;
		this.customerRepository = customerRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public RatingDto addRating(Integer productId, RatingDto ratingDto) throws NotFoundExceptionRequest {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Customer customer = customerRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User "
						+ "not found with username: "+username));
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new NotFoundExceptionRequest("Product with id "+
						productId+" not found"));
		Rating rating = new Rating(ratingDto.getValue(),
				new Timestamp(System.currentTimeMillis()),customer,product);
		
		Integer quantityRatings = product.getQuantityRatings();
		product.setQuantityRatings(quantityRatings+1);

		ratingDto = modelMapper.map(rating, RatingDto.class);
		
		ratingRepository.save(rating);
		productRepository.save(product);
		return ratingDto;
	}

	@Override
	public void deleteRating(Integer ratingId) throws NotFoundExceptionRequest, NullPointerException {
		Rating rating = ratingRepository.findById(ratingId).orElseThrow(
				() -> new NotFoundExceptionRequest("Rating with id "+
						ratingId+" not found"));
		ratingRepository.delete(rating);
	}

	@Override
	public List<RatingDto> getRatings(Integer productId) throws NotFoundExceptionRequest {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new NotFoundExceptionRequest("Product with id "+
						productId+" not found"));
		return product.getRatings().stream().map(
				rating -> modelMapper.map(rating, RatingDto.class)).collect(Collectors.toList());
	}

}

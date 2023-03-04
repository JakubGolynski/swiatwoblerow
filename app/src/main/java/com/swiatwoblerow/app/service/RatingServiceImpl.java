package com.swiatwoblerow.app.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.swiatwoblerow.app.dto.RatingDto;
import com.swiatwoblerow.app.entity.Customer;
import com.swiatwoblerow.app.entity.Product;
import com.swiatwoblerow.app.entity.Rating;
import com.swiatwoblerow.app.exceptions.CustomerIsNotOwnerException;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;
import com.swiatwoblerow.app.exceptions.TooManyInsertException;
import com.swiatwoblerow.app.repository.CustomerRepository;
import com.swiatwoblerow.app.repository.ProductRepository;
import com.swiatwoblerow.app.repository.RatingRepository;
import com.swiatwoblerow.app.service.filter.RatingFilter;
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
	public List<RatingDto> getRatings(Integer productId, RatingFilter ratingFilter) throws NotFoundExceptionRequest {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new NotFoundExceptionRequest("Product with id "+
						productId+" not found"));
		
		int startIndex = ratingFilter.getPage()*ratingFilter.getSize();
		int endIndex = Math.min(startIndex + ratingFilter.getSize(), product.getRatings().size());
		if((startIndex < 0) || (startIndex >= product.getRatings().size())) {
			return new ArrayList<>();
		}
		return product.getRatings().stream().map(
				rating -> modelMapper.map(rating, RatingDto.class)).collect(Collectors.toList())
				.subList(startIndex, endIndex);
	}

	@Override
	public RatingDto addRating(Integer productId, RatingDto ratingDto) throws NotFoundExceptionRequest,TooManyInsertException {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Customer customer = customerRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User "
						+ "not found with username: "+username));
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new NotFoundExceptionRequest("Product with id "+
						productId+" not found"));
		
		
		Rating existingRating = ratingRepository.findByOwnerAndProduct(customer, product).orElse(null);
		if(existingRating != null) {
			throw new TooManyInsertException("Customer can add only one rating to certain product,"
					+ " existing ratingId: "+existingRating.getId());
		}
		Rating rating = new Rating(ratingDto.getValue(),
				new Timestamp(System.currentTimeMillis()),customer,product);
		ratingRepository.save(rating);
		
		Integer quantityRatings = product.getQuantityRatings();
		product.setQuantityRatings(quantityRatings+1);
		
		productRepository.save(product);
		RatingDto returnRatingDto = modelMapper.map(rating, RatingDto.class);
		return returnRatingDto;
	}

	@Override
	public void deleteRating(Integer ratingId) throws NotFoundExceptionRequest, NullPointerException,CustomerIsNotOwnerException {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Customer customer = customerRepository.findByUsername(username).orElseThrow(
				() -> new UsernameNotFoundException("User not found with username: "+ username));
		Rating rating = ratingRepository.findById(ratingId).orElseThrow(
				() -> new NotFoundExceptionRequest("Rating with id "+
						ratingId+" not found"));
		if(!rating.getOwner().getUsername().equals(customer.getUsername())) {
			throw new CustomerIsNotOwnerException("Customer can only delete ratings that he owns");
		}
		ratingRepository.delete(rating);
	}

}

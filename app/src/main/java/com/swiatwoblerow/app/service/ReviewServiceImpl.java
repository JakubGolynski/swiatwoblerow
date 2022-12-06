package com.swiatwoblerow.app.service;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.swiatwoblerow.app.dto.ReviewDto;
import com.swiatwoblerow.app.entity.Customer;
import com.swiatwoblerow.app.entity.Product;
import com.swiatwoblerow.app.entity.Review;
import com.swiatwoblerow.app.exceptions.CustomerIsNotOwnerException;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;
import com.swiatwoblerow.app.exceptions.TooManyInsertException;
import com.swiatwoblerow.app.repository.CustomerRepository;
import com.swiatwoblerow.app.repository.ProductRepository;
import com.swiatwoblerow.app.repository.ReviewRepository;
import com.swiatwoblerow.app.service.filter.ReviewFilter;
import com.swiatwoblerow.app.service.interfaces.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {
	
	private ReviewRepository reviewRepository;
	private ProductRepository productRepository;
	private CustomerRepository customerRepository;
	private ModelMapper modelMapper;

	public ReviewServiceImpl(ReviewRepository reviewRepository, ProductRepository productRepository,
			CustomerRepository customerRepository, ModelMapper modelMapper) {
		this.reviewRepository = reviewRepository;
		this.productRepository = productRepository;
		this.customerRepository = customerRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public List<ReviewDto> getReviews(Integer productId, ReviewFilter reviewFilter) throws NotFoundExceptionRequest{
		Pageable pageable = PageRequest.of(reviewFilter.getPage(),reviewFilter.getSize());
		
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new NotFoundExceptionRequest("Product with id "+
						productId+" not found"));
		return reviewRepository.findAllByProduct(product,pageable).stream()
				.map(review -> modelMapper.map(review, ReviewDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public ReviewDto addReview(Integer productId, ReviewDto reviewDto) throws UsernameNotFoundException,NotFoundExceptionRequest,TooManyInsertException{
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Customer customer = customerRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User "
						+ "not found with username: "+username));
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new NotFoundExceptionRequest("Product with id "+
						productId+" not found"));
		
		boolean customerAlreadyAddedReview = reviewRepository.existsByOwnerAndProduct(customer, product);
		if(customerAlreadyAddedReview) {
			throw new TooManyInsertException("Customer can add only one review to certain product");
		}
		Review review = new Review();
		review.setMessage(reviewDto.getMessage());
		review.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		review.setQuantityThumbsUp(0);
		review.setQuantityThumbsDown(0);
		review.setOwner(customer);
		review.setProduct(product);
		review.setReviewOwner(username);
		reviewRepository.save(review);
		
		product.getReviews().add(review);
		product.setQuantityReviews(product.getQuantityReviews()+1);
		productRepository.save(product);
		
		ReviewDto returnReviewDto = modelMapper.map(review, ReviewDto.class);
		return returnReviewDto;
	}
	
	@Override
	public void deleteReview(Integer reviewId) throws NotFoundExceptionRequest, NullPointerException,CustomerIsNotOwnerException {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Customer customer = customerRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User "
						+ "not found with username: "+username));
		Review review = reviewRepository.findById(reviewId).orElseThrow(
				() -> new NotFoundExceptionRequest("Review with id "+
						reviewId+" not found"));
		
		if(!review.getOwner().getUsername().equals(customer.getUsername())) {
			throw new CustomerIsNotOwnerException("Customer can only delete reviews that he owns");
		}
		reviewRepository.delete(review);
		return;
	}

	@Override
	public void addThumbUp(Integer reviewId) throws UsernameNotFoundException, NotFoundExceptionRequest,TooManyInsertException {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Customer customer = customerRepository.findByUsername(username).orElseThrow(
				() -> new UsernameNotFoundException("User not found with username "+ username));
		Review review = reviewRepository.findById(reviewId).orElseThrow(
				() -> new NotFoundExceptionRequest("Review with id "+
						reviewId+" not found"));
		Set<Customer> customersWhoMaybeLikedReview = new HashSet<>();
		customersWhoMaybeLikedReview.add(customer);
		
		boolean customerAlreadyLikedReview = reviewRepository
				.existsByIdAndCustomersWhoLikedReviewIn(reviewId, customersWhoMaybeLikedReview);
		boolean customerAlreadyDislikedReview = reviewRepository
				.existsByIdAndCustomersWhoDislikedReviewIn(reviewId, customersWhoMaybeLikedReview);
		
		if(customerAlreadyLikedReview) {
			throw new TooManyInsertException("Customer can add only one thumb up to certain review");
		}
		if(customerAlreadyDislikedReview) {
			int quantityThumbsDown = review.getQuantityThumbsDown();
			review.getCustomersWhoDislikedReview().remove(customer);
			review.setQuantityThumbsDown(quantityThumbsDown-1);
		}
		
		int quantityThumbsUp = review.getQuantityThumbsUp();
		review.getCustomersWhoLikedReview().add(customer);
		review.setQuantityThumbsUp(quantityThumbsUp+1);
		reviewRepository.save(review);
		return;
	}

	@Override
	public void deleteThumbUp(Integer reviewId) throws UsernameNotFoundException,NotFoundExceptionRequest, NullPointerException,CustomerIsNotOwnerException{
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Customer customer = customerRepository.findByUsername(username).orElseThrow(
				() -> new UsernameNotFoundException("User not found with username "+username));
		Review review = reviewRepository.findById(reviewId).orElseThrow(
				() -> new NotFoundExceptionRequest("Review with id "+
						reviewId+" not found"));
		Set<Customer> customersWhoMaybeLikedReview = new HashSet<>();
		customersWhoMaybeLikedReview.add(customer);
		
		boolean customerOwnsThumbUp = reviewRepository
				.existsByIdAndCustomersWhoLikedReviewIn(reviewId, customersWhoMaybeLikedReview);
		
		if(customerOwnsThumbUp == false) {
			throw new CustomerIsNotOwnerException("Customer does not own thumb up "
					+ "in review with reviewId: "+ reviewId);
		}
		
		int quantityThumbsUp = review.getQuantityThumbsUp();
		review.getCustomersWhoLikedReview().remove(customer);
		review.setQuantityThumbsUp(quantityThumbsUp-1);
		reviewRepository.save(review);
		return;
	}

	@Override
	public void addThumbDown(Integer reviewId) throws UsernameNotFoundException, NotFoundExceptionRequest,TooManyInsertException {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Customer customer = customerRepository.findByUsername(username).orElseThrow(
				() -> new UsernameNotFoundException("User not found with username "+ username));
		Review review = reviewRepository.findById(reviewId).orElseThrow(
				() -> new NotFoundExceptionRequest("Review with id "+
						reviewId+" not found"));
		Set<Customer> customersWhoMaybeDislikedReview = new HashSet<>();
		customersWhoMaybeDislikedReview.add(customer);
		boolean customerAlreadyDisikedReview = reviewRepository
				.existsByIdAndCustomersWhoDislikedReviewIn(reviewId, customersWhoMaybeDislikedReview);
		boolean customerAlreadyLikedReview = reviewRepository
				.existsByIdAndCustomersWhoLikedReviewIn(reviewId, customersWhoMaybeDislikedReview);
		
		if(customerAlreadyDisikedReview) {
			throw new TooManyInsertException("Customer can add only one thumb down to certain review");
		}
		if(customerAlreadyLikedReview) {
			int quantityThumbsUp = review.getQuantityThumbsUp();
			review.getCustomersWhoLikedReview().remove(customer);
			review.setQuantityThumbsUp(quantityThumbsUp-1);
		}
		
		int quantityThumbsDown = review.getQuantityThumbsDown();
		review.getCustomersWhoDislikedReview().add(customer);
		review.setQuantityThumbsDown(quantityThumbsDown+1);
		reviewRepository.save(review);
		return;
	}

	@Override
	public void deleteThumbDown(Integer reviewId) throws UsernameNotFoundException,NotFoundExceptionRequest,NullPointerException,CustomerIsNotOwnerException {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Customer customer = customerRepository.findByUsername(username).orElseThrow(
				() -> new UsernameNotFoundException("User not found with username "+username));
		Review review = reviewRepository.findById(reviewId).orElseThrow(
				() -> new NotFoundExceptionRequest("Review with id "+
						reviewId+" not found"));
		Set<Customer> customersWhoMaybeDislikedReview = new HashSet<>();
		customersWhoMaybeDislikedReview.add(customer);
		
		boolean customerOwnsThumbDown = reviewRepository
				.existsByIdAndCustomersWhoDislikedReviewIn(reviewId, customersWhoMaybeDislikedReview);
		
		if(customerOwnsThumbDown == false) {
			throw new CustomerIsNotOwnerException("Customer does not own thumb down "
					+ "in review with reviewId: "+ reviewId);
		}
		
		int quantityThumbsDown = review.getQuantityThumbsDown();
		review.getCustomersWhoDislikedReview().remove(customer);
		review.setQuantityThumbsDown(quantityThumbsDown-1);
		reviewRepository.save(review);
		return;
	}

}

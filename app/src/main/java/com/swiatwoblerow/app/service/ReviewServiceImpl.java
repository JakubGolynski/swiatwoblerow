package com.swiatwoblerow.app.service;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.swiatwoblerow.app.dto.ReviewDto;
import com.swiatwoblerow.app.dto.ThumbDto;
import com.swiatwoblerow.app.entity.Customer;
import com.swiatwoblerow.app.entity.Product;
import com.swiatwoblerow.app.entity.Review;
import com.swiatwoblerow.app.exceptions.CustomerIsNotOwnerException;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;
import com.swiatwoblerow.app.exceptions.TooManyInsertException;
import com.swiatwoblerow.app.repository.CustomerRepository;
import com.swiatwoblerow.app.repository.ProductRepository;
import com.swiatwoblerow.app.repository.ReviewRepository;
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
	public List<ReviewDto> getReviews(Integer productId) throws NotFoundExceptionRequest{
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new NotFoundExceptionRequest("Product with id "+
						productId+" not found"));
		return reviewRepository.findAllByProduct(product).stream()
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
		
		Review existingReview = reviewRepository.findByOwnerAndProduct(customer, product).orElse(null);
		if(existingReview != null) {
			throw new TooManyInsertException("Customer can add only one review to certain product,"
					+ " existing reviewId: "+existingReview.getId());
		}
		Review review = new Review();
		review.setMessage(reviewDto.getMessage());
		review.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		review.setQuantityThumbsUp(0);
		review.setQuantityThumbsDown(0);
		review.setOwner(customer);
		review.setProduct(product);
		reviewRepository.save(review);
		
		ReviewDto returnReviewDto = modelMapper.map(review, ReviewDto.class);
		return returnReviewDto;
	}
	
	@Override
	public void deleteReview(Integer reviewId) throws NotFoundExceptionRequest, NullPointerException,CustomerIsNotOwnerException {
		CustomerPrincipal customerPrincipal = (CustomerPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Customer customer = customerRepository.findByUsername(customerPrincipal.getUsername()).orElseThrow(
				() -> new UsernameNotFoundException("User not found with username "+ customerPrincipal.getUsername()));
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
		int countCustomerWhoAddedThumbUp = reviewRepository
				.countByIdAndCustomersWhoLikedReviewIn(reviewId, customersWhoMaybeLikedReview);
		int countCustomerWhoAddedThumbDown = reviewRepository
				.countByIdAndCustomersWhoDislikedReviewIn(reviewId, customersWhoMaybeLikedReview);
		
		if(countCustomerWhoAddedThumbUp > 0) {
			throw new TooManyInsertException("Customer can add only one thumb up to certain review");
		}
		if(countCustomerWhoAddedThumbDown > 0) {
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
		CustomerPrincipal customerPrincipal = (CustomerPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Customer customer = customerRepository.findByUsername(customerPrincipal.getUsername()).orElseThrow(
				() -> new UsernameNotFoundException("User not found with username "+ customerPrincipal.getUsername()));
		Review review = reviewRepository.findById(reviewId).orElseThrow(
				() -> new NotFoundExceptionRequest("Review with id "+
						reviewId+" not found"));
		Set<Customer> customersWhoMaybeLikedReview = new HashSet<>();
		customersWhoMaybeLikedReview.add(customer);
		int countCustomerWhoAddedThumbUp = reviewRepository
				.countByIdAndCustomersWhoLikedReviewIn(reviewId, customersWhoMaybeLikedReview);
		
		if(countCustomerWhoAddedThumbUp <= 0) {
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
		int countCustomerWhoAddedThumbDown = reviewRepository
				.countByIdAndCustomersWhoDislikedReviewIn(reviewId, customersWhoMaybeDislikedReview);
		int countCustomerWhoAddedThumbUp = reviewRepository
				.countByIdAndCustomersWhoLikedReviewIn(reviewId, customersWhoMaybeDislikedReview);
		
		if(countCustomerWhoAddedThumbDown > 0) {
			throw new TooManyInsertException("Customer can add only one thumb down to certain review");
		}
		if(countCustomerWhoAddedThumbUp > 0) {
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
		CustomerPrincipal customerPrincipal = (CustomerPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Customer customer = customerRepository.findByUsername(customerPrincipal.getUsername()).orElseThrow(
				() -> new UsernameNotFoundException("User not found with username "+ customerPrincipal.getUsername()));
		Review review = reviewRepository.findById(reviewId).orElseThrow(
				() -> new NotFoundExceptionRequest("Review with id "+
						reviewId+" not found"));
		Set<Customer> customersWhoMaybeDislikedReview = new HashSet<>();
		customersWhoMaybeDislikedReview.add(customer);
		int countCustomerWhoAddedThumDown = reviewRepository
				.countByIdAndCustomersWhoLikedReviewIn(reviewId, customersWhoMaybeDislikedReview);
		
		if(countCustomerWhoAddedThumDown <= 0) {
			throw new CustomerIsNotOwnerException("Customer does not own thumb down "
					+ "in review with reviewId: "+ reviewId);
		}
		
		int quantityThumbsDown = review.getQuantityThumbsDown();
		review.getCustomersWhoDislikedReview().remove(customer);
		review.setQuantityThumbsDown(quantityThumbsDown-1);
		reviewRepository.save(review);
		return;
	}

	@Override
	public ThumbDto getReviewThumbs(Integer reviewId) throws NotFoundExceptionRequest{
		Review review = reviewRepository.findById(reviewId).orElseThrow(
				() -> new NotFoundExceptionRequest("Review with id "+
						reviewId+" not found"));
		ThumbDto thumbDto = new ThumbDto(
				review.getCustomersWhoLikedReview().stream().map(
						customer -> customer.getUsername()).collect(Collectors.toSet()),
				review.getCustomersWhoDislikedReview().stream().map(
						customer -> customer.getUsername()).collect(Collectors.toSet()));
		return thumbDto;
	}

}

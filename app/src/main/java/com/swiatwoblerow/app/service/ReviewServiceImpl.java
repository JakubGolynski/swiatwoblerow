package com.swiatwoblerow.app.service;

import java.sql.Timestamp;
import java.util.List;
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
		
		List<Review> reviewList = product.getReviews();
		if(this.hasCustomerAlreadyAddedReview(reviewList, username)) {
			throw new TooManyInsertException("Customer can add only one review to certain product");
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
	public void deleteReview(Integer reviewId) throws NotFoundExceptionRequest, NullPointerException {
		Review review = reviewRepository.findById(reviewId).orElseThrow(
				() -> new NotFoundExceptionRequest("Review with id "+
						reviewId+" not found"));
		reviewRepository.delete(review);
		return;
	}

	@Override
	public void addThumbUp(Integer reviewId) throws UsernameNotFoundException, NotFoundExceptionRequest {
		CustomerPrincipal customerPrincipal = (CustomerPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Customer customer = customerRepository.findByUsername(customerPrincipal.getUsername()).orElseThrow(
				() -> new UsernameNotFoundException("User not found with username "+ customerPrincipal.getUsername()));
		Review review = reviewRepository.findById(reviewId).orElseThrow(
				() -> new NotFoundExceptionRequest("Review with id "+
						reviewId+" not found"));
		int quantityThumbsUp = review.getQuantityThumbsUp();
		
		if(review.getCustomersWhoLikedReview().contains(customer) == false) {
			quantityThumbsUp = quantityThumbsUp+1;
		}
		
		review.getCustomersWhoLikedReview().add(customer);
		review.setQuantityThumbsUp(quantityThumbsUp);
		reviewRepository.save(review);
		return;
	}

	@Override
	public void deleteThumbUp(Integer reviewId, Integer thumbUpId) throws UsernameNotFoundException,NotFoundExceptionRequest {
		CustomerPrincipal customerPrincipal = (CustomerPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Customer customer = customerRepository.findByUsername(customerPrincipal.getUsername()).orElseThrow(
				() -> new UsernameNotFoundException("User not found with username "+ customerPrincipal.getUsername()));
		Review review = reviewRepository.findById(reviewId).orElseThrow(
				() -> new NotFoundExceptionRequest("Review with id "+
						reviewId+" not found"));
		int quantityThumbsUp = review.getQuantityThumbsUp();
		
		if(review.getCustomersWhoLikedReview().contains(customer)) {
			quantityThumbsUp = quantityThumbsUp-1;
		}
		
		review.getCustomersWhoLikedReview().remove(customer);
		review.setQuantityThumbsUp(quantityThumbsUp);
		reviewRepository.save(review);
		return;
	}

	@Override
	public void addThumbDown(Integer reviewId) throws UsernameNotFoundException, NotFoundExceptionRequest {
		CustomerPrincipal customerPrincipal = (CustomerPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Customer customer = customerRepository.findByUsername(customerPrincipal.getUsername()).orElseThrow(
				() -> new UsernameNotFoundException("User not found with username "+ customerPrincipal.getUsername()));
		Review review = reviewRepository.findById(reviewId).orElseThrow(
				() -> new NotFoundExceptionRequest("Review with id "+
						reviewId+" not found"));
		int quantityThumbsDown = review.getQuantityThumbsDown();
		
		if(review.getCustomersWhoDislikedReview().contains(customer) == false) {
			quantityThumbsDown = quantityThumbsDown+1;
		}
		
		review.getCustomersWhoLikedReview().add(customer);
		review.setQuantityThumbsUp(quantityThumbsDown);
		reviewRepository.save(review);
		return;
	}

	@Override
	public void deleteThumbDown(Integer reviewId, Integer thumbDownId) throws UsernameNotFoundException,NotFoundExceptionRequest {
		CustomerPrincipal customerPrincipal = (CustomerPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Customer customer = customerRepository.findByUsername(customerPrincipal.getUsername()).orElseThrow(
				() -> new UsernameNotFoundException("User not found with username "+ customerPrincipal.getUsername()));
		Review review = reviewRepository.findById(reviewId).orElseThrow(
				() -> new NotFoundExceptionRequest("Review with id "+
						reviewId+" not found"));
		
		int quantityThumbsDown = review.getQuantityThumbsDown();
		
		if(review.getCustomersWhoDislikedReview().contains(customer)) {
			quantityThumbsDown = quantityThumbsDown-1;
		}
		
		review.getCustomersWhoLikedReview().remove(customer);
		review.setQuantityThumbsUp(quantityThumbsDown);
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
						customer -> customer.getUsername()).collect(Collectors.toList()),
				review.getCustomersWhoDislikedReview().stream().map(
						customer -> customer.getUsername()).collect(Collectors.toList()));
		return thumbDto;
	}
	
	@Override
	public boolean hasCustomerAlreadyAddedReview(List<Review> reviewList, String customerUsername) {
		List<String> customerUsernames = reviewList.stream().map(
				review -> review.getOwner().getUsername()).collect(Collectors.toList());
		
		return customerUsernames.contains(customerUsername);
	}

}

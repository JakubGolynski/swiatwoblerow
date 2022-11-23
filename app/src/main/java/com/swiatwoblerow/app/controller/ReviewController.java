package com.swiatwoblerow.app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.swiatwoblerow.app.dto.ReviewDto;
import com.swiatwoblerow.app.dto.ThumbDto;
import com.swiatwoblerow.app.exceptions.CustomerIsNotOwnerException;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;
import com.swiatwoblerow.app.exceptions.TooManyInsertException;
import com.swiatwoblerow.app.service.filter.ReviewFilter;
import com.swiatwoblerow.app.service.interfaces.ReviewService;

@RestController
@RequestMapping
public class ReviewController {
	
	private ReviewService reviewService;
	
	public ReviewController(ReviewService reviewService) {
		this.reviewService = reviewService;
	}
	
	@GetMapping("/products/{id}/reviews")
	public List<ReviewDto> getReviews(@PathVariable int id, @RequestBody ReviewFilter reviewFilter) throws NotFoundExceptionRequest{
		return reviewService.getReviews(id,reviewFilter);
	}
	
	@PostMapping("/products/{id}/reviews")
	@ResponseStatus(value = HttpStatus.CREATED)
	public ReviewDto addReview(@PathVariable int id,@RequestBody ReviewDto reviewDto) throws UsernameNotFoundException,NotFoundExceptionRequest,TooManyInsertException{
		return reviewService.addReview(id,reviewDto);
	}
	
	@DeleteMapping("/products/{productId}/reviews/{reviewId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteReview(@PathVariable int reviewId) throws NotFoundExceptionRequest, NullPointerException,CustomerIsNotOwnerException {
		reviewService.deleteReview(reviewId);
	}
	
	@PostMapping("/products/{productId}/reviews/{reviewId}/thumbsup")
	@ResponseStatus(value = HttpStatus.CREATED)
	public void addThumbUp(@PathVariable int reviewId) throws UsernameNotFoundException, NotFoundExceptionRequest,TooManyInsertException{
		reviewService.addThumbUp(reviewId);
	}
	
	@DeleteMapping("/products/{productId}/reviews/{reviewId}/thumbsup")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteThumbUp(@PathVariable int reviewId) throws NotFoundExceptionRequest,CustomerIsNotOwnerException{
		reviewService.deleteThumbUp(reviewId);
	}
	
	@PostMapping("/products/{productId}/reviews/{reviewId}/thumbsdown")
	@ResponseStatus(value = HttpStatus.CREATED)
	public void addThumbDown(@PathVariable int reviewId) throws NotFoundExceptionRequest,TooManyInsertException{
		reviewService.addThumbDown(reviewId);
	}
	
	@DeleteMapping("/products/{productId}/reviews/{reviewId}/thumbsdown")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteThumbDown(@PathVariable int reviewId) throws NotFoundExceptionRequest,CustomerIsNotOwnerException{
		reviewService.deleteThumbDown(reviewId);
	}
	
	@GetMapping("/products/{productId}/reviews/{reviewId}/thumbs")
	public ThumbDto getThumbs(@PathVariable int reviewId) throws NotFoundExceptionRequest{
		return reviewService.getReviewThumbs(reviewId);
	}
}

package com.swiatwoblerow.app.unitests.servicetests;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.swiatwoblerow.app.dto.ReviewDto;
import com.swiatwoblerow.app.entity.Address;
import com.swiatwoblerow.app.entity.Category;
import com.swiatwoblerow.app.entity.Condition;
import com.swiatwoblerow.app.entity.Country;
import com.swiatwoblerow.app.entity.Customer;
import com.swiatwoblerow.app.entity.Product;
import com.swiatwoblerow.app.entity.Review;
import com.swiatwoblerow.app.entity.Role;
import com.swiatwoblerow.app.exceptions.CustomerIsNotOwnerException;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;
import com.swiatwoblerow.app.exceptions.TooManyInsertException;
import com.swiatwoblerow.app.repository.CustomerRepository;
import com.swiatwoblerow.app.repository.ProductRepository;
import com.swiatwoblerow.app.repository.ReviewRepository;
import com.swiatwoblerow.app.service.ReviewServiceImpl;
import com.swiatwoblerow.app.service.filter.ReviewFilter;
import com.swiatwoblerow.app.service.interfaces.ReviewService;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {
	
	@Mock
	private ReviewRepository reviewRepository;
	@Mock
	private ProductRepository productRepository;
	@Mock
	private CustomerRepository customerRepository;
	private ModelMapper modelMapper = new ModelMapper();
	private ReviewService reviewService;
	private Product product;
	private Customer customer;
	private ReviewFilter reviewFilter;
	
	@BeforeEach
	void setUp(){
		reviewService = new ReviewServiceImpl(reviewRepository,productRepository,
				customerRepository, modelMapper);
		Integer id = 1;
		product = new Product();
		product.setId(id);
		product.setName("test!@#Product");
		product.setPrice(5.0);
		product.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		product.setQuantity(1);
		product.setMessage("test!@#Product");
		product.setRating(5.0);
		product.setOwner(customer);
		product.setQuantityReviews(0);
		product.setQuantityRatings(0);

		Condition condition = new Condition("USED");
		product.setCondition(condition);

		Category category = new Category("test!@#Product");
		product.setCategory(category);
		
		customer = new Customer();
		String customerName = "test!@#łUsername";
		customer.setId(id);
		customer.setUsername(customerName);
		customer.setPassword("testłPassword");
		customer.setFirstName("testł!@#");
		customer.setLastName("testł!@#");
		String customerEmail = "testł!@#";
		customer.setEmail(customerEmail);
		customer.setTelephone("+48512806005");
		
		Address address = new Address();
		address.setCity("Warsaw");
		address.setStreet("Piłsudskiego");
		address.setHouseNumber("471A");
		
		Country country = new Country("Poland");
		address.setCountry(country);
		customer.setAddress(address);
		
		Role roleUser = new Role("ROLE_USER");
		customer.setRole(roleUser);
		
		reviewFilter = new ReviewFilter(0,5);
	}
	
	@Test
	public void getReviewsSuccess() throws Exception{
		Integer id = 1;
		Review review = new Review();
		review.setId(id);
		review.setMessage("test123!@#");
		review.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		review.setQuantityThumbsDown(0);
		review.setQuantityThumbsUp(0);
		review.setOwner(customer);
		review.setProduct(product);
		
		Set<Customer> customersWhoLikedReview = new HashSet<>();
		Set<Customer> customersWhoDislikedReview = new HashSet<>();
		
		review.setCustomersWhoLikedReview(customersWhoLikedReview);
		review.setCustomersWhoDislikedReview(customersWhoDislikedReview);
		List<Review> listOfReviews = new ArrayList<>();
		listOfReviews.add(review);
		
		Pageable pageable = PageRequest.of(0, 5);
		
		when(productRepository.findById(id)).thenReturn(Optional.of(product));
		when(reviewRepository.findByProduct(product,pageable)).thenReturn(listOfReviews);

		List<ReviewDto> listOfReviewDtos = listOfReviews.stream()
				.map(currentReview -> modelMapper.map(currentReview, ReviewDto.class))
				.collect(Collectors.toList());
		assertThat(reviewService.getReviews(id,reviewFilter))
				.usingRecursiveComparison()
				.isEqualTo(listOfReviewDtos);
		
	}
	
	@Test
	public void getReviewsFailBadProductId() {
		Integer id = 11111123;
		NotFoundExceptionRequest exception = assertThrows(NotFoundExceptionRequest.class,
				() -> {
					reviewService.getReviews(id,reviewFilter);
				});
		assertThat(exception.getMessage()).isEqualTo(
				"Product with id "+id+" not found");
		assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
	}
	
	@Test
	public void addReviewSuccess() throws Exception{
		Integer id = 1;
		Review review = new Review();
		review.setMessage("test123!@#");
		review.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		review.setQuantityThumbsDown(0);
		review.setQuantityThumbsUp(0);

		when(customerRepository.findByUsername(customer.getUsername())).thenReturn(Optional.of(customer));
		when(productRepository.findById(id)).thenReturn(Optional.of(product));
		when(reviewRepository.existsByOwnerAndProduct(customer, product)).thenReturn(false);
		
		Authentication authentication = Mockito.mock(Authentication.class);
		when(authentication.getName()).thenReturn(customer.getUsername());
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		
		ReviewDto reviewDto = modelMapper.map(review, ReviewDto.class);
		ReviewDto returnedReviewDto = reviewService.addReview(id, reviewDto);

		reviewDto.setReviewOwner(customer.getUsername());
		
		assertThat(new Date(reviewDto.getCreatedAt().getTime()))
		.isBeforeOrEqualTo(new Date(returnedReviewDto.getCreatedAt().getTime()));
	
		reviewDto.setCreatedAt(null);
		returnedReviewDto.setCreatedAt(null);
	
		assertThat(returnedReviewDto)
				.usingRecursiveComparison()
				.ignoringFields("id")
				.isEqualTo(reviewDto);
		assertThat(returnedReviewDto).isNotNull();
	}
	
	@Test
	public void addReviewFailUserAlreadyHasReview() {
		Integer id = 1;
		ReviewDto reviewDto = new ReviewDto();
		reviewDto.setMessage("test123!@#");
		reviewDto.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		reviewDto.setQuantityThumbsDown(0);
		reviewDto.setQuantityThumbsUp(0);

		when(customerRepository.findByUsername(customer.getUsername())).thenReturn(Optional.of(customer));
		when(productRepository.findById(id)).thenReturn(Optional.of(product));
		when(reviewRepository.existsByOwnerAndProduct(customer, product)).thenReturn(true);
		
		Authentication authentication = Mockito.mock(Authentication.class);
		when(authentication.getName()).thenReturn(customer.getUsername());
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		
		TooManyInsertException exception = assertThrows(TooManyInsertException.class,
				() -> {
					reviewService.addReview(id,reviewDto);
				});
		assertThat(exception.getMessage()).isEqualTo(
				"Customer can add only one review to certain product");
		assertThat(exception.getStatus()).isEqualTo(HttpStatus.CONFLICT);

	}
	
	@Test
	public void addReviewFailBadProductId() {
		Integer id = 1;
		ReviewDto reviewDto = new ReviewDto();
		reviewDto.setMessage("test123!@#");
		reviewDto.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		reviewDto.setQuantityThumbsDown(0);
		reviewDto.setQuantityThumbsUp(0);

		when(customerRepository.findByUsername(customer.getUsername())).thenReturn(Optional.of(customer));
		
		Authentication authentication = Mockito.mock(Authentication.class);
		when(authentication.getName()).thenReturn(customer.getUsername());
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		
		NotFoundExceptionRequest exception = assertThrows(NotFoundExceptionRequest.class,
				() -> {
					reviewService.addReview(id,reviewDto);
				});
		assertThat(exception.getMessage()).isEqualTo(
				"Product with id "+id+" not found");
		assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
	}
	
	@Test
	public void deleteReviewSuccess() throws Exception{
		Integer id = 1;
		Review review = new Review();
		review.setId(id);
		review.setMessage("test123!@#");
		review.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		review.setQuantityThumbsDown(0);
		review.setQuantityThumbsUp(0);
		review.setOwner(customer);
		review.setProduct(product);

		when(customerRepository.findByUsername(customer.getUsername())).thenReturn(Optional.of(customer));
		when(reviewRepository.findById(id)).thenReturn(Optional.of(review));
		
		Authentication authentication = Mockito.mock(Authentication.class);
		when(authentication.getName()).thenReturn(customer.getUsername());
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		
		assertDoesNotThrow(() -> reviewService.deleteReview(id));
	}
	
	@Test
	public void deleteReviewFailUserDoesNotOwnThisReview() {
		Integer id = 1;
		Review review = new Review();
		review.setId(id);
		review.setMessage("test123!@#");
		review.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		review.setQuantityThumbsDown(0);
		review.setQuantityThumbsUp(0);
		review.setOwner(customer);
		review.setProduct(product);
		
		Customer badCustomer = new Customer();
		badCustomer.setUsername("doesNotOwnTheReview");

		when(customerRepository.findByUsername(customer.getUsername())).thenReturn(Optional.of(badCustomer));
		when(reviewRepository.findById(id)).thenReturn(Optional.of(review));
		
		Authentication authentication = Mockito.mock(Authentication.class);
		when(authentication.getName()).thenReturn(customer.getUsername());
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		
		CustomerIsNotOwnerException exception = assertThrows(CustomerIsNotOwnerException.class,
				() -> {
					reviewService.deleteReview(id);
				});
		assertThat(exception.getMessage()).isEqualTo(
				"Customer can only delete reviews that he owns");
		assertThat(exception.getStatus()).isEqualTo(HttpStatus.CONFLICT);
	}
	
	@Test
	public void deleteReviewFailBadReviewId() {
		Integer id = 1;
		
		when(customerRepository.findByUsername(customer.getUsername())).thenReturn(Optional.of(customer));
		
		Authentication authentication = Mockito.mock(Authentication.class);
		when(authentication.getName()).thenReturn(customer.getUsername());
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		
		NotFoundExceptionRequest exception = assertThrows(NotFoundExceptionRequest.class,
				() -> {
					reviewService.deleteReview(id);
				});
		assertThat(exception.getMessage()).isEqualTo(
				"Review with id "+id+" not found");
		assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
	}
	
	@Test
	public void addThumbUpSuccess() throws Exception{
		Integer reviewId = 1;
		Review review = new Review();
		review.setId(reviewId);
		review.setMessage("test123!@#");
		review.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		review.setQuantityThumbsDown(0);
		review.setQuantityThumbsUp(0);
		review.setOwner(customer);
		review.setProduct(product);

		when(customerRepository.findByUsername(customer.getUsername())).thenReturn(Optional.of(customer));
		when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
		
		Authentication authentication = Mockito.mock(Authentication.class);
		when(authentication.getName()).thenReturn(customer.getUsername());
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		
		Set<Customer> customersWhoMaybeLikedReview = new HashSet<>();
		customersWhoMaybeLikedReview.add(customer);
		
		int quantityThumbsUp = review.getQuantityThumbsUp();
		int quantityThumbsDown = review.getQuantityThumbsDown();
		
		reviewService.addThumbUp(reviewId);
		
		
		assertThat(review.getQuantityThumbsUp()).isEqualTo(quantityThumbsUp+1);
		assertThat(review.getQuantityThumbsDown()).isEqualTo(quantityThumbsDown);
		assertThat(review.getCustomersWhoLikedReview()).isEqualTo(customersWhoMaybeLikedReview);
	}
	
	@Test
	public void addThumbUpSuccessCustomerAlreadyHasThumbDown() throws Exception{
		Integer reviewId = 1;
		
		Set<Customer> customersDislikedReview = new HashSet<>();
		customersDislikedReview.add(customer);
		
		Review review = new Review();
		review.setId(reviewId);
		review.setMessage("test123!@#");
		review.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		review.setQuantityThumbsDown(1);
		review.setQuantityThumbsUp(0);
		review.setCustomersWhoDislikedReview(customersDislikedReview);
		review.setOwner(customer);
		review.setProduct(product);

		when(customerRepository.findByUsername(customer.getUsername())).thenReturn(Optional.of(customer));
		when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
		when(reviewRepository
				.existsByIdAndCustomersWhoDislikedReviewIn(reviewId, customersDislikedReview))
			.thenReturn(true);
		
		Authentication authentication = Mockito.mock(Authentication.class);
		when(authentication.getName()).thenReturn(customer.getUsername());
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		
		Set<Customer> customersLikedReview = new HashSet<>();
		customersLikedReview.add(customer);
		
		int quantityThumbsUp = review.getQuantityThumbsUp();
		int quantityThumbsDown = review.getQuantityThumbsDown();
		
		reviewService.addThumbUp(reviewId);
		
		assertThat(review.getQuantityThumbsUp()).isEqualTo(quantityThumbsUp+1);
		assertThat(review.getQuantityThumbsDown()).isEqualTo(quantityThumbsDown-1);
		assertThat(review.getCustomersWhoLikedReview()).isEqualTo(customersLikedReview);
		assertThat(review.getCustomersWhoDislikedReview()).isEmpty();
	}
	
	@Test
	public void addThumbUpFailCustomerAlreadyHasThumbUp() {
		Integer reviewId = 1;
		
		Set<Customer> customersLikedReview = new HashSet<>();
		customersLikedReview.add(customer);
		
		Review review = new Review();
		review.setId(reviewId);
		review.setMessage("test123!@#");
		review.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		review.setQuantityThumbsDown(0);
		review.setQuantityThumbsUp(1);
		review.setCustomersWhoLikedReview(customersLikedReview);
		review.setOwner(customer);
		review.setProduct(product);

		when(customerRepository.findByUsername(customer.getUsername())).thenReturn(Optional.of(customer));
		when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
		when(reviewRepository
				.existsByIdAndCustomersWhoLikedReviewIn(reviewId, customersLikedReview))
			.thenReturn(true);
		
		Authentication authentication = Mockito.mock(Authentication.class);
		when(authentication.getName()).thenReturn(customer.getUsername());
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		
		TooManyInsertException exception = assertThrows(TooManyInsertException.class,
				() -> {
					reviewService.addThumbUp(reviewId);
				});
		assertThat(exception.getMessage()).isEqualTo(
				"Customer can add only one thumb up to certain review");
		assertThat(exception.getStatus()).isEqualTo(HttpStatus.CONFLICT);
	}
	
	@Test
	public void addThumbUpFailBadReviewId() {
		Integer reviewId = 1;

		when(customerRepository.findByUsername(customer.getUsername())).thenReturn(Optional.of(customer));
		
		Authentication authentication = Mockito.mock(Authentication.class);
		when(authentication.getName()).thenReturn(customer.getUsername());
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		
		NotFoundExceptionRequest exception = assertThrows(NotFoundExceptionRequest.class,
				() -> {
					reviewService.addThumbUp(reviewId);
				});
		assertThat(exception.getMessage()).isEqualTo(
				"Review with id "+reviewId+" not found");
		assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
	}
	
	@Test
	public void deleteThumbUpSuccess() throws Exception{
		Integer reviewId = 1;
		
		Set<Customer> customersLikedReview = new HashSet<>();
		customersLikedReview.add(customer);
		
		Review review = new Review();
		review.setId(reviewId);
		review.setMessage("test123!@#");
		review.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		review.setQuantityThumbsDown(0);
		review.setQuantityThumbsUp(1);
		review.setCustomersWhoLikedReview(customersLikedReview);
		review.setOwner(customer);
		review.setProduct(product);

		when(customerRepository.findByUsername(customer.getUsername())).thenReturn(Optional.of(customer));
		when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
		when(reviewRepository
				.existsByIdAndCustomersWhoLikedReviewIn(reviewId, customersLikedReview))
			.thenReturn(true);
		
		Authentication authentication = Mockito.mock(Authentication.class);
		when(authentication.getName()).thenReturn(customer.getUsername());
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		
		int quantityThumbsUp = review.getQuantityThumbsUp();
		int quantityThumbsDown = review.getQuantityThumbsDown();
		
		reviewService.deleteThumbUp(reviewId);
		
		assertThat(review.getQuantityThumbsUp()).isEqualTo(quantityThumbsUp-1);
		assertThat(review.getQuantityThumbsDown()).isEqualTo(quantityThumbsDown);
		assertThat(review.getCustomersWhoLikedReview()).isEmpty();
		assertThat(review.getCustomersWhoDislikedReview()).isEmpty();
	}
	
	@Test
	public void deleteThumbUpFailBadReviewId() {
		Integer reviewId = 1;

		when(customerRepository.findByUsername(customer.getUsername())).thenReturn(Optional.of(customer));
		
		Authentication authentication = Mockito.mock(Authentication.class);
		when(authentication.getName()).thenReturn(customer.getUsername());
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		
		NotFoundExceptionRequest exception = assertThrows(NotFoundExceptionRequest.class,
				() -> {
					reviewService.deleteThumbUp(reviewId);
				});
		assertThat(exception.getMessage()).isEqualTo(
				"Review with id "+reviewId+" not found");
		assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
	}
	
	@Test
	public void deleteThumbUpFailInThisReviewUserDoesNotOwnThumbUp() {
		Integer reviewId = 1;
		
		Set<Customer> customersLikedReview = new HashSet<>();
		customersLikedReview.add(customer);
		
		Review review = new Review();
		review.setId(reviewId);
		review.setMessage("test123!@#");
		review.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		review.setQuantityThumbsDown(0);
		review.setQuantityThumbsUp(1);
		review.setCustomersWhoLikedReview(customersLikedReview);
		review.setOwner(customer);
		review.setProduct(product);

		when(customerRepository.findByUsername(customer.getUsername())).thenReturn(Optional.of(customer));
		when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
		
		Authentication authentication = Mockito.mock(Authentication.class);
		when(authentication.getName()).thenReturn(customer.getUsername());
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		
		int quantityThumbsUp = review.getQuantityThumbsUp();
		int quantityThumbsDown = review.getQuantityThumbsDown();
		
		CustomerIsNotOwnerException exception = assertThrows(CustomerIsNotOwnerException.class,
				() -> {
					reviewService.deleteThumbUp(reviewId);
				});
		assertThat(exception.getMessage()).isEqualTo("Customer does not own thumb up "
						+ "in review with reviewId: "+ reviewId);
		
		assertThat(exception.getStatus()).isEqualTo(HttpStatus.CONFLICT);
		assertThat(review.getQuantityThumbsUp()).isEqualTo(quantityThumbsUp);
		assertThat(review.getQuantityThumbsDown()).isEqualTo(quantityThumbsDown);
		assertThat(review.getCustomersWhoLikedReview()).isNotEmpty();
	}
	
	@Test
	public void addThumbDownSuccess() throws Exception{
		Integer reviewId = 1;
		Review review = new Review();
		review.setId(reviewId);
		review.setMessage("test123!@#");
		review.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		review.setQuantityThumbsDown(0);
		review.setQuantityThumbsUp(0);
		review.setOwner(customer);
		review.setProduct(product);

		when(customerRepository.findByUsername(customer.getUsername())).thenReturn(Optional.of(customer));
		when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
		
		Authentication authentication = Mockito.mock(Authentication.class);
		when(authentication.getName()).thenReturn(customer.getUsername());
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		
		Set<Customer> customersWhoMaybeDislikedReview = new HashSet<>();
		customersWhoMaybeDislikedReview.add(customer);
		
		int quantityThumbsUp = review.getQuantityThumbsUp();
		int quantityThumbsDown = review.getQuantityThumbsDown();
		
		reviewService.addThumbDown(reviewId);
		
		assertThat(review.getQuantityThumbsDown()).isEqualTo(quantityThumbsDown+1);
		assertThat(review.getQuantityThumbsUp()).isEqualTo(quantityThumbsUp);
		assertThat(review.getCustomersWhoDislikedReview()).isEqualTo(customersWhoMaybeDislikedReview);
	}
	
	@Test
	public void addThumbDownSuccessCustomerAlreadyHasThumbUp() throws Exception{
		Integer reviewId = 1;
		
		Set<Customer> customersLikedReview = new HashSet<>();
		customersLikedReview.add(customer);
		
		Review review = new Review();
		review.setId(reviewId);
		review.setMessage("test123!@#");
		review.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		review.setQuantityThumbsDown(0);
		review.setQuantityThumbsUp(1);
		review.setCustomersWhoLikedReview(customersLikedReview);
		review.setOwner(customer);
		review.setProduct(product);

		when(customerRepository.findByUsername(customer.getUsername())).thenReturn(Optional.of(customer));
		when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
		when(reviewRepository
				.existsByIdAndCustomersWhoLikedReviewIn(reviewId, customersLikedReview))
			.thenReturn(true);
		
		Authentication authentication = Mockito.mock(Authentication.class);
		when(authentication.getName()).thenReturn(customer.getUsername());
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		
		Set<Customer> customersDislikedReview = new HashSet<>();
		customersDislikedReview.add(customer);
		
		int quantityThumbsUp = review.getQuantityThumbsUp();
		int quantityThumbsDown = review.getQuantityThumbsDown();
		
		reviewService.addThumbDown(reviewId);
		
		assertThat(review.getQuantityThumbsUp()).isEqualTo(quantityThumbsUp-1);
		assertThat(review.getQuantityThumbsDown()).isEqualTo(quantityThumbsDown+1);
		assertThat(review.getCustomersWhoDislikedReview()).isEqualTo(customersDislikedReview);
		assertThat(review.getCustomersWhoLikedReview()).isEmpty();
	}
	
	@Test
	public void addThumbDownFailCustomerAlreadyHasThumbDown() {
		Integer reviewId = 1;
		
		Set<Customer> customersDislikedReview = new HashSet<>();
		customersDislikedReview.add(customer);
		
		Review review = new Review();
		review.setId(reviewId);
		review.setMessage("test123!@#");
		review.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		review.setQuantityThumbsDown(1);
		review.setQuantityThumbsUp(0);
		review.setCustomersWhoDislikedReview(customersDislikedReview);
		review.setOwner(customer);
		review.setProduct(product);

		when(customerRepository.findByUsername(customer.getUsername())).thenReturn(Optional.of(customer));
		when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
		when(reviewRepository
				.existsByIdAndCustomersWhoDislikedReviewIn(reviewId, customersDislikedReview))
			.thenReturn(true);
		
		Authentication authentication = Mockito.mock(Authentication.class);
		when(authentication.getName()).thenReturn(customer.getUsername());
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		
		TooManyInsertException exception = assertThrows(TooManyInsertException.class,
				() -> {
					reviewService.addThumbDown(reviewId);
				});
		assertThat(exception.getMessage()).isEqualTo(
				"Customer can add only one thumb down to certain review");
		assertThat(exception.getStatus()).isEqualTo(HttpStatus.CONFLICT);
	}
	
	@Test
	public void addThumbDownFailBadReviewId() {
		Integer reviewId = 1;

		when(customerRepository.findByUsername(customer.getUsername())).thenReturn(Optional.of(customer));
		
		Authentication authentication = Mockito.mock(Authentication.class);
		when(authentication.getName()).thenReturn(customer.getUsername());
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		
		NotFoundExceptionRequest exception = assertThrows(NotFoundExceptionRequest.class,
				() -> {
					reviewService.addThumbDown(reviewId);
				});
		assertThat(exception.getMessage()).isEqualTo(
				"Review with id "+reviewId+" not found");
		assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
	}
	
	@Test
	public void deleteThumbDownSuccess() throws Exception{
		Integer reviewId = 1;
		
		Set<Customer> customersDislikedReview = new HashSet<>();
		customersDislikedReview.add(customer);
		
		Review review = new Review();
		review.setId(reviewId);
		review.setMessage("test123!@#");
		review.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		review.setQuantityThumbsDown(1);
		review.setQuantityThumbsUp(0);
		review.setCustomersWhoDislikedReview(customersDislikedReview);
		review.setOwner(customer);
		review.setProduct(product);

		when(customerRepository.findByUsername(customer.getUsername())).thenReturn(Optional.of(customer));
		when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
		when(reviewRepository
				.existsByIdAndCustomersWhoDislikedReviewIn(reviewId, customersDislikedReview))
			.thenReturn(true);
		
		Authentication authentication = Mockito.mock(Authentication.class);
		when(authentication.getName()).thenReturn(customer.getUsername());
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		
		int quantityThumbsUp = review.getQuantityThumbsUp();
		int quantityThumbsDown = review.getQuantityThumbsDown();
		
		reviewService.deleteThumbDown(reviewId);
		
		assertThat(review.getQuantityThumbsUp()).isEqualTo(quantityThumbsUp);
		assertThat(review.getQuantityThumbsDown()).isEqualTo(quantityThumbsDown-1);
		assertThat(review.getCustomersWhoLikedReview()).isEmpty();
		assertThat(review.getCustomersWhoDislikedReview()).isEmpty();
	}
	
	@Test
	public void deleteThumbDownFailBadReviewId() {
		Integer reviewId = 1;

		when(customerRepository.findByUsername(customer.getUsername())).thenReturn(Optional.of(customer));
		
		Authentication authentication = Mockito.mock(Authentication.class);
		when(authentication.getName()).thenReturn(customer.getUsername());
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		
		NotFoundExceptionRequest exception = assertThrows(NotFoundExceptionRequest.class,
				() -> {
					reviewService.deleteThumbDown(reviewId);
				});
		assertThat(exception.getMessage()).isEqualTo(
				"Review with id "+reviewId+" not found");
		assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
	}
	
	@Test
	public void deleteThumbDownFailInThisReviewUserDoesNotOwnAnyThumbDown() {
		Integer reviewId = 1;
		
		Set<Customer> customersDislikedReview = new HashSet<>();
		customersDislikedReview.add(customer);
		
		Review review = new Review();
		review.setId(reviewId);
		review.setMessage("test123!@#");
		review.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		review.setQuantityThumbsDown(1);
		review.setQuantityThumbsUp(0);
		review.setCustomersWhoDislikedReview(customersDislikedReview);
		review.setOwner(customer);
		review.setProduct(product);

		when(customerRepository.findByUsername(customer.getUsername())).thenReturn(Optional.of(customer));
		when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
		
		Authentication authentication = Mockito.mock(Authentication.class);
		when(authentication.getName()).thenReturn(customer.getUsername());
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		
		int quantityThumbsUp = review.getQuantityThumbsUp();
		int quantityThumbsDown = review.getQuantityThumbsDown();
		
		CustomerIsNotOwnerException exception = assertThrows(CustomerIsNotOwnerException.class,
				() -> {
					reviewService.deleteThumbDown(reviewId);
				});
		assertThat(exception.getMessage()).isEqualTo("Customer does not own thumb down "
						+ "in review with reviewId: "+ reviewId);
		
		assertThat(exception.getStatus()).isEqualTo(HttpStatus.CONFLICT);
		assertThat(review.getQuantityThumbsUp()).isEqualTo(quantityThumbsUp);
		assertThat(review.getQuantityThumbsDown()).isEqualTo(quantityThumbsDown);
		assertThat(review.getCustomersWhoDislikedReview()).isNotEmpty();
	}
}

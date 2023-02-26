package com.swiatwoblerow.app.unitests.servicetests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.swiatwoblerow.app.dto.RatingDto;
import com.swiatwoblerow.app.entity.Address;
import com.swiatwoblerow.app.entity.Category;
import com.swiatwoblerow.app.entity.Condition;
import com.swiatwoblerow.app.entity.Country;
import com.swiatwoblerow.app.entity.Customer;
import com.swiatwoblerow.app.entity.Product;
import com.swiatwoblerow.app.entity.Rating;
import com.swiatwoblerow.app.entity.Role;
import com.swiatwoblerow.app.exceptions.CustomerIsNotOwnerException;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;
import com.swiatwoblerow.app.exceptions.TooManyInsertException;
import com.swiatwoblerow.app.repository.CustomerRepository;
import com.swiatwoblerow.app.repository.ProductRepository;
import com.swiatwoblerow.app.repository.RatingRepository;
import com.swiatwoblerow.app.service.RatingServiceImpl;
import com.swiatwoblerow.app.service.interfaces.RatingService;

@ExtendWith(MockitoExtension.class)
public class RatingServiceTest {
	
	@Mock
	private ProductRepository productRepository;
	@Mock
	private RatingRepository ratingRepository;
	@Mock
	private CustomerRepository customerRepository;
	
	private ModelMapper modelMapper = new ModelMapper();
	private RatingService ratingService;
	private Product product;
	private Customer customer;
	
	@BeforeEach
	void setUp(){
		ratingService = new RatingServiceImpl(productRepository,ratingRepository,customerRepository,
				modelMapper);
		
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
		
		Set<Condition> conditions = new HashSet<>();
		conditions.add(new Condition("USED"));
		conditions.add(new Condition("DAMAGED"));
		
		product.setConditions(conditions);
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
		Set<Category> managedCategories = new HashSet<>();
		customer.setManagedCategories(managedCategories);
	}
	
	@Test
	public void getRatingsSuccess() throws Exception{
		Integer id = 1;
		Rating rating = new Rating();
		rating.setId(id);
		rating.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		rating.setOwner(customer);
		rating.setProduct(product);
		rating.setValue((short)5);
		
		List<Rating> ratings = new ArrayList<>();
		ratings.add(rating);
		
		product.setRatings(ratings);
		
		when(productRepository.findById(id)).thenReturn(Optional.of(product));		
		
		List<RatingDto> ratingsDtos = ratings.stream()
				.map(currentRating -> modelMapper.map(currentRating, RatingDto.class))
				.collect(Collectors.toList());
		
		assertThat(ratingService.getRatings(id)).isEqualTo(ratingsDtos);
	}
	
	@Test
	public void getRatingsFailBadProductId() throws Exception{
		Integer id = 11111123;
		NotFoundExceptionRequest exception = assertThrows(NotFoundExceptionRequest.class,
				() -> {
					ratingService.getRatings(id);
				});
		
		assertThat(exception).isExactlyInstanceOf(NotFoundExceptionRequest.class);
		assertThat(exception.getMessage()).isEqualTo(
				"Product with id "+id+" not found");
		assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
	}
	
	@Test
	public void addRatingSuccess() throws Exception{
		Integer id = 1;
		Rating rating = new Rating();
		rating.setId(id);
		rating.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		rating.setOwner(customer);
		rating.setProduct(product);
		rating.setValue((short)5);
		
		Integer quantityRatingsBefore = product.getQuantityRatings();
		
		when(customerRepository.findByUsername(customer.getUsername())).thenReturn(Optional.of(customer));
		when(productRepository.findById(id)).thenReturn(Optional.of(product));
		
		Authentication authentication = Mockito.mock(Authentication.class);
		when(authentication.getName()).thenReturn(customer.getUsername());
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		
		RatingDto ratingDto = modelMapper.map(rating, RatingDto.class);
		
		RatingDto returnedRatingDto = ratingService.addRating(id, ratingDto);
		
		returnedRatingDto.setId(id);
		
		assertThat(new Date(ratingDto.getCreatedAt().getTime()))
		.isBeforeOrEqualTo(new Date(returnedRatingDto.getCreatedAt().getTime()));
		
		ratingDto.setCreatedAt(null);
		returnedRatingDto.setCreatedAt(null);
		
		assertThat(ratingDto).isEqualTo(returnedRatingDto);
		assertThat(returnedRatingDto).isNotNull();
		assertThat(quantityRatingsBefore+1).isEqualTo(product.getQuantityRatings());
	}
	
	@Test
	public void addRatingFailBadUsername() throws Exception{
		Integer id = 1;
		Rating rating = new Rating();
		rating.setId(id);
		rating.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		rating.setOwner(customer);
		rating.setProduct(product);
		rating.setValue((short)5);
		
		RatingDto ratingDto = modelMapper.map(rating, RatingDto.class);
		
		Authentication authentication = Mockito.mock(Authentication.class);
		when(authentication.getName()).thenReturn(customer.getUsername());
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		
		UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
				() -> {
					ratingService.addRating(id, ratingDto);
				});
		
		assertThat(exception).isExactlyInstanceOf(UsernameNotFoundException.class);
		assertThat(exception.getMessage()).isEqualTo("User not found with username: "
				+ customer.getUsername());
	}
	
	@Test
	public void addRatingFailBadProductId() throws Exception{
		Integer id = 1;
		Rating rating = new Rating();
		rating.setId(id);
		rating.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		rating.setOwner(customer);
		rating.setProduct(product);
		rating.setValue((short)5);
		
		RatingDto ratingDto = modelMapper.map(rating, RatingDto.class);
		
		Authentication authentication = Mockito.mock(Authentication.class);
		when(authentication.getName()).thenReturn(customer.getUsername());
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		
		when(customerRepository.findByUsername(customer.getUsername())).thenReturn(Optional.of(customer));
		
		NotFoundExceptionRequest exception = assertThrows(NotFoundExceptionRequest.class,
				() -> {
					ratingService.addRating(id, ratingDto);
				});
		
		assertThat(exception).isExactlyInstanceOf(NotFoundExceptionRequest.class);
		assertThat(exception.getMessage()).isEqualTo(
				"Product with id "+id+" not found");
		assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
	}
	
	@Test
	public void addRatingFailUserAlreadyHasRating() throws Exception{
		Integer id = 1;
		Rating rating = new Rating();
		rating.setId(id);
		rating.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		rating.setOwner(customer);
		rating.setProduct(product);
		rating.setValue((short)5);
		
		RatingDto ratingDto = modelMapper.map(rating, RatingDto.class);
		
		Authentication authentication = Mockito.mock(Authentication.class);
		when(authentication.getName()).thenReturn(customer.getUsername());
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		
		when(customerRepository.findByUsername(customer.getUsername())).thenReturn(Optional.of(customer));
		when(productRepository.findById(id)).thenReturn(Optional.of(product));
		
		Rating existingRating = new Rating();
		existingRating.setId(id);
		existingRating.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		existingRating.setOwner(customer);
		existingRating.setProduct(product);
		existingRating.setValue((short)4);
		
		when(ratingRepository.findByOwnerAndProduct(customer, product)).thenReturn(Optional.of(existingRating));
		
		TooManyInsertException exception = assertThrows(TooManyInsertException.class,
				() -> {
					ratingService.addRating(id, ratingDto);
				});
		
		assertThat(exception).isExactlyInstanceOf(TooManyInsertException.class);
		assertThat(exception.getMessage()).isEqualTo(
				"Customer can add only one rating to certain product,"
						+ " existing ratingId: "+existingRating.getId());
		assertThat(exception.getStatus()).isEqualTo(HttpStatus.CONFLICT);
	}
	
	@Test
	public void deleteRatingSuccess() throws Exception{
		Integer id = 1;
		Rating rating = new Rating();
		rating.setId(id);
		rating.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		rating.setOwner(customer);
		rating.setProduct(product);
		rating.setValue((short)5);
		
		Authentication authentication = Mockito.mock(Authentication.class);
		when(authentication.getName()).thenReturn(customer.getUsername());
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		
		when(customerRepository.findByUsername(customer.getUsername())).thenReturn(Optional.of(customer));
		when(ratingRepository.findById(id)).thenReturn(Optional.of(rating));
		
		assertDoesNotThrow(() -> ratingService.deleteRating(id));
	}
	
	@Test
	public void deleteRatingFailBadUsername() throws Exception{
		Integer id = 1;
		Rating rating = new Rating();
		rating.setId(id);
		rating.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		rating.setOwner(customer);
		rating.setProduct(product);
		rating.setValue((short)5);
		
		Authentication authentication = Mockito.mock(Authentication.class);
		when(authentication.getName()).thenReturn(customer.getUsername());
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		
		UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
				() -> {
					ratingService.deleteRating(id);
				});
		
		assertThat(exception).isExactlyInstanceOf(UsernameNotFoundException.class);
		assertThat(exception.getMessage()).isEqualTo("User not found with username: "
				+ customer.getUsername());
	}
	
	@Test
	public void deleteRatingFailBadRatingId() throws Exception{
		Integer id = 1;
		Rating rating = new Rating();
		rating.setId(id);
		rating.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		rating.setOwner(customer);
		rating.setProduct(product);
		rating.setValue((short)5);
		
		Authentication authentication = Mockito.mock(Authentication.class);
		when(authentication.getName()).thenReturn(customer.getUsername());
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		
		when(customerRepository.findByUsername(customer.getUsername())).thenReturn(Optional.of(customer));
		
		NotFoundExceptionRequest exception = assertThrows(NotFoundExceptionRequest.class,
				() -> {
					ratingService.deleteRating(id);
				});
		
		assertThat(exception).isExactlyInstanceOf(NotFoundExceptionRequest.class);
		assertThat(exception.getMessage()).isEqualTo("Rating with id "+
				id+" not found");
		assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
	}
	
	@Test
	public void deleteRatingFailUserDoesNotOwnThisRating() throws Exception{
		Integer id = 1;
		Rating rating = new Rating();
		rating.setId(id);
		rating.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		rating.setOwner(customer);
		rating.setProduct(product);
		rating.setValue((short)5);
		
		Authentication authentication = Mockito.mock(Authentication.class);
		when(authentication.getName()).thenReturn(customer.getUsername());
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		
		when(customerRepository.findByUsername(customer.getUsername())).thenReturn(Optional.of(customer));
		
		Customer anotherCustomer = new Customer();
		anotherCustomer.setUsername("AnotherUsernameCustomer");
		
		Rating anotherUserRating = new Rating();
		anotherUserRating.setId(id+1);
		anotherUserRating.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		anotherUserRating.setOwner(anotherCustomer);
		anotherUserRating.setProduct(product);
		anotherUserRating.setValue((short)5);
		
		when(ratingRepository.findById(id)).thenReturn(Optional.of(anotherUserRating));
		
		CustomerIsNotOwnerException exception = assertThrows(CustomerIsNotOwnerException.class,
				() -> {
					ratingService.deleteRating(id);
				});
		
		assertThat(exception).isExactlyInstanceOf(CustomerIsNotOwnerException.class);
		assertThat(exception.getMessage()).isEqualTo("Customer can only delete ratings that he owns");
		assertThat(exception.getStatus()).isEqualTo(HttpStatus.CONFLICT);
	}
	
}

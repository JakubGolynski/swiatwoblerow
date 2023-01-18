package com.swiatwoblerow.app.unitests.servicetests;

import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.swiatwoblerow.app.entity.Address;
import com.swiatwoblerow.app.entity.Category;
import com.swiatwoblerow.app.entity.Condition;
import com.swiatwoblerow.app.entity.Country;
import com.swiatwoblerow.app.entity.Customer;
import com.swiatwoblerow.app.entity.Product;
import com.swiatwoblerow.app.entity.Rating;
import com.swiatwoblerow.app.entity.Role;
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
		//N+1 PROBLEM NOT SOLVED
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
	public void getRatingsSuccess() {
		Integer id = 1;
		Rating rating = new Rating();
		rating.setId(id);
		rating.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		rating.setOwner(customer);
		rating.setProduct(product);
		rating.setValue((short)5);
		
		when(productRepository.findById(id)).thenReturn(Optional.of(product));
	}
	
	@Test
	public void getRatingsFailBadProductId() {
		//TO-DO
	}
	
	@Test
	public void addRatingSuccess() {
		//TO-DO
	}
	
	@Test
	public void addRatingFailBadUsername() {
		//TO-DO
	}
	
	@Test
	public void addRatingFailBadProductId() {
		//TO-DO
	}
	
	@Test
	public void addRatingFailUserAlreadyHasRating() {
		//TO-DO
	}
	///////
	
	@Test
	public void deleteRatingSuccess() {
		//TO-DO
	}
	
	@Test
	public void deleteRatingFailBadUsername() {
		//TO-DO
	}
	
	@Test
	public void deleteRatingFailBadRatingId() {
		//TO-DO
	}
	
	@Test
	public void deleteRatingFailUserDoesNotOwnThisRating() {
		//TO-DO
	}
	
}

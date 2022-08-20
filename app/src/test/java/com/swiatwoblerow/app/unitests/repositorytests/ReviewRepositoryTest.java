package com.swiatwoblerow.app.unitests.repositorytests;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.swiatwoblerow.app.entity.Address;
import com.swiatwoblerow.app.entity.Country;
import com.swiatwoblerow.app.entity.Customer;
import com.swiatwoblerow.app.entity.Product;
import com.swiatwoblerow.app.entity.Review;
import com.swiatwoblerow.app.entity.Role;
import com.swiatwoblerow.app.repository.AddressRepository;
import com.swiatwoblerow.app.repository.CountryRepository;
import com.swiatwoblerow.app.repository.CustomerRepository;
import com.swiatwoblerow.app.repository.ProductRepository;
import com.swiatwoblerow.app.repository.ReviewRepository;
import com.swiatwoblerow.app.repository.RoleRepository;

@DataJpaTest
public class ReviewRepositoryTest {

	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Test
	public void shouldFindReviewByOwnerAndProductIfExists(){
		
		Customer customer = new Customer();
		String customerName = "test!@#łUsername";
		customer.setUsername(customerName);
		customer.setPassword("testłPassword");
		customer.setFirstName("testł!@#");
		customer.setLastName("testł!@#");
		customer.setEmail("testł!@#");
		customer.setTelephone("+48512806005");
		
		Address address = new Address();
		address.setCity("Warsaw");
		address.setStreet("Piłsudskiego");
		address.setHouseNumber("471A");
		addressRepository.save(address);
		
		Country country = new Country("Poland");
		countryRepository.save(country);
		address.setCountry(country);
		customer.setAddress(address);
		
		Role roleUser = new Role("ROLE_USER");
		roleRepository.save(roleUser);
		
		Set<Role> roles = new HashSet<>();
		roles.add(roleUser);
		customer.setRoles(roles);
		customerRepository.save(customer);

		Product product = new Product();
		product.setName("testProduct123!@#");
		product.setPrice(5.0);
		product.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		product.setQuantity(2);
		product.setMessage("testProduct123!@#");
		product.setRating(5.0);
		product.setOwner(customer);
		productRepository.save(product);
		
		Review review = new Review();
		review.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		review.setOwner(customer);
		review.setProduct(product);
		review.setMessage("test!@#123");
		reviewRepository.save(review);
		
		Review reviewFromDatabase = reviewRepository.findByOwnerAndProduct(customer,product).orElse(null);
		
		assertThat(reviewFromDatabase).isEqualTo(review);
	}
	
	@Test
	public void shouldFindAllReviewsByProductIfExists(){
		
		Customer customer1 = new Customer();
		String customerName = "test!@#łUsername";
		customer1.setUsername(customerName);
		customer1.setPassword("testłPassword");
		customer1.setFirstName("testł!@#");
		customer1.setLastName("testł!@#");
		customer1.setEmail("testł!@#");
		customer1.setTelephone("+48512806005");
		
		Address address = new Address();
		address.setCity("Warsaw");
		address.setStreet("Piłsudskiego");
		address.setHouseNumber("471A");
		addressRepository.save(address);
		
		Country country = new Country("Poland");
		countryRepository.save(country);
		address.setCountry(country);
		customer1.setAddress(address);
		
		Role roleUser = new Role("ROLE_USER");
		roleRepository.save(roleUser);
		
		Set<Role> roles = new HashSet<>();
		roles.add(roleUser);
		customer1.setRoles(roles);
		customerRepository.save(customer1);

		Product product = new Product();
		product.setName("testProduct123!@#");
		product.setPrice(5.0);
		product.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		product.setQuantity(2);
		product.setMessage("testProduct123!@#");
		product.setRating(5.0);
		product.setOwner(customer1);
		productRepository.save(product);
		
		Review review1 = new Review();
		review1.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		review1.setOwner(customer1);
		review1.setProduct(product);
		review1.setMessage("test!@#123");
		reviewRepository.save(review1);
		
		Customer customer2 = new Customer();
		customer2.setUsername(customerName);
		customer2.setPassword("testłPassword");
		customer2.setFirstName("testł!@#");
		customer2.setLastName("testł!@#");
		customer2.setEmail("testł!@#");
		customer2.setTelephone("+48512806005");
		customer2.setAddress(address);
		customer2.setRoles(roles);
		customerRepository.save(customer2);
		
		Review review2 = new Review();
		review2.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		review2.setOwner(customer2);
		review2.setProduct(product);
		review2.setMessage("test!@#123");
		reviewRepository.save(review2);
		
		
		List<Review> expectedReviews = new ArrayList<>();
		expectedReviews.add(review1);
		expectedReviews.add(review2);
		
		List<Review> reviewsFromDatabase = reviewRepository.findAllByProduct(product);
		
		assertThat(reviewsFromDatabase).isEqualTo(expectedReviews);
	}
	
	@Test
	public void shouldCountByIdAndCustomersWhoLikedReviewInIfExists(){
		
		Customer customer1 = new Customer();
		String customerName = "test!@#łUsername";
		customer1.setUsername(customerName);
		customer1.setPassword("testłPassword");
		customer1.setFirstName("testł!@#");
		customer1.setLastName("testł!@#");
		customer1.setEmail("testł!@#");
		customer1.setTelephone("+48512806005");
		
		Address address = new Address();
		address.setCity("Warsaw");
		address.setStreet("Piłsudskiego");
		address.setHouseNumber("471A");
		addressRepository.save(address);
		
		Country country = new Country("Poland");
		countryRepository.save(country);
		address.setCountry(country);
		customer1.setAddress(address);
		
		Role roleUser = new Role("ROLE_USER");
		roleRepository.save(roleUser);
		
		Set<Role> roles = new HashSet<>();
		roles.add(roleUser);
		customer1.setRoles(roles);
		customerRepository.save(customer1);

		Product product = new Product();
		product.setName("testProduct123!@#");
		product.setPrice(5.0);
		product.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		product.setQuantity(2);
		product.setMessage("testProduct123!@#");
		product.setRating(5.0);
		product.setOwner(customer1);
		productRepository.save(product);
		
		Review review = new Review();
		review.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		review.setOwner(customer1);
		review.setProduct(product);
		review.setMessage("test!@#123");
		review.getCustomersWhoLikedReview().add(customer1);
		review.setQuantityThumbsUp(review.getQuantityThumbsUp()+1);
		reviewRepository.save(review);
		
		int expectedNumberOfCustomerLikes = 1;
		
		int numberOfCustomerLikes= reviewRepository.countByIdAndCustomersWhoLikedReviewIn(review.getId(), review.getCustomersWhoLikedReview());

		assertThat(expectedNumberOfCustomerLikes).isEqualTo(numberOfCustomerLikes);
	}
	
	@Test
	public void shouldcountByIdAndCustomersWhoDislikedReviewInIfExists(){
		
		Customer customer1 = new Customer();
		String customerName = "test!@#łUsername";
		customer1.setUsername(customerName);
		customer1.setPassword("testłPassword");
		customer1.setFirstName("testł!@#");
		customer1.setLastName("testł!@#");
		customer1.setEmail("testł!@#");
		customer1.setTelephone("+48512806005");
		
		Address address = new Address();
		address.setCity("Warsaw");
		address.setStreet("Piłsudskiego");
		address.setHouseNumber("471A");
		addressRepository.save(address);
		
		Country country = new Country("Poland");
		countryRepository.save(country);
		address.setCountry(country);
		customer1.setAddress(address);
		
		Role roleUser = new Role("ROLE_USER");
		roleRepository.save(roleUser);
		
		Set<Role> roles = new HashSet<>();
		roles.add(roleUser);
		customer1.setRoles(roles);
		customerRepository.save(customer1);

		Product product = new Product();
		product.setName("testProduct123!@#");
		product.setPrice(5.0);
		product.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		product.setQuantity(2);
		product.setMessage("testProduct123!@#");
		product.setRating(5.0);
		product.setOwner(customer1);
		productRepository.save(product);
		
		Review review = new Review();
		review.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		review.setOwner(customer1);
		review.setProduct(product);
		review.setMessage("test!@#123");
		review.getCustomersWhoDislikedReview().add(customer1);
		review.setQuantityThumbsDown(review.getQuantityThumbsDown()+1);
		reviewRepository.save(review);
		
		int expectedNumberOfCustomerDislikes = 1;
		
		int numberOfCustomerDislikes= reviewRepository.countByIdAndCustomersWhoDislikedReviewIn(review.getId(), review.getCustomersWhoDislikedReview());

		assertThat(expectedNumberOfCustomerDislikes).isEqualTo(numberOfCustomerDislikes);
	}
}

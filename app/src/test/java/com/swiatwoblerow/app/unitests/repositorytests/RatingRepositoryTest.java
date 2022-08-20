package com.swiatwoblerow.app.unitests.repositorytests;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.swiatwoblerow.app.entity.Address;
import com.swiatwoblerow.app.entity.Country;
import com.swiatwoblerow.app.entity.Customer;
import com.swiatwoblerow.app.entity.Product;
import com.swiatwoblerow.app.entity.Rating;
import com.swiatwoblerow.app.entity.Role;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;
import com.swiatwoblerow.app.repository.AddressRepository;
import com.swiatwoblerow.app.repository.CountryRepository;
import com.swiatwoblerow.app.repository.CustomerRepository;
import com.swiatwoblerow.app.repository.ProductRepository;
import com.swiatwoblerow.app.repository.RatingRepository;
import com.swiatwoblerow.app.repository.RoleRepository;

@DataJpaTest
public class RatingRepositoryTest {
	
	@Autowired
	private RatingRepository ratingRepository;
	
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
	public void shouldFindRatingIfExists() throws NotFoundExceptionRequest,UsernameNotFoundException{
		
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
		
		Rating rating = new Rating();
		rating.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		rating.setOwner(customer);
		rating.setProduct(product);
		rating.setValue((short)5);
		ratingRepository.save(rating);
		
		Rating ratingFromDatabase = ratingRepository.findByOwnerAndProduct(customer,product).orElse(null);
		
		assertThat(ratingFromDatabase).isEqualTo(rating);
	}
	

	@Test
	public void shouldReturnNullIfRepositoryIsEmpty() {
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
		
		Rating expectedRating = ratingRepository.findByOwnerAndProduct(customer,product).orElse(null);
		
		assertThat(expectedRating).isNull();
	}
}

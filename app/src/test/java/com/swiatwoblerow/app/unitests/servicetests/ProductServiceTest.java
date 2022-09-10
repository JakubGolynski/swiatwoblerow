package com.swiatwoblerow.app.unitests.servicetests;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


import static org.mockito.Mockito.when;


import com.swiatwoblerow.app.dto.ProductDto;
import com.swiatwoblerow.app.entity.Address;
import com.swiatwoblerow.app.entity.Category;
import com.swiatwoblerow.app.entity.Condition;
import com.swiatwoblerow.app.entity.Country;
import com.swiatwoblerow.app.entity.Customer;
import com.swiatwoblerow.app.entity.Product;
import com.swiatwoblerow.app.entity.Role;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;
import com.swiatwoblerow.app.repository.CategoryRepository;
import com.swiatwoblerow.app.repository.ConditionRepository;
import com.swiatwoblerow.app.repository.CustomerRepository;
import com.swiatwoblerow.app.repository.ProductRepository;
import com.swiatwoblerow.app.service.ProductServiceImpl;
import com.swiatwoblerow.app.service.interfaces.ProductService;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private CategoryRepository categoryRepository;

	@Mock
	private ConditionRepository conditionRepository;
	
	private ModelMapper modelMapper = new ModelMapper();
	
	private ProductService productService;
	
	@BeforeEach
	void setUp() {
		productService = new ProductServiceImpl(productRepository,customerRepository,categoryRepository,
				conditionRepository,modelMapper);
	}

	
	@Test
	public void getProductsSuccess() throws Exception{
		//TO-DO
	}
	
	@Test
	public void getProductSuccess() throws Exception{
		Product product = new Product();
		product.setName("test!@#Product");
		product.setPrice(5.0);
		product.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		product.setQuantity(1);
		product.setMessage("test!@#Product");
		product.setRating(5.0);
		
		Set<Condition> conditions = new HashSet<>();
		conditions.add(new Condition("USED"));
		conditions.add(new Condition("DAMAGED"));
		
		product.setConditions(conditions);
		Category category = new Category("test!@#Product");
		product.setCategory(category);
		
		Customer customer = new Customer();
		String customerName = "test!@#łUsername";
		Integer id = 1;
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
		Set<Role> roles = new HashSet<>();
		roles.add(roleUser);
		customer.setRoles(roles);
		product.setOwner(customer);
		product.setQuantityReviews(0);
		product.setQuantityRatings(0);
		
		when(productRepository.findById(id)).thenReturn(Optional.of(product));
		
		ProductDto productDto = modelMapper.map(product, ProductDto.class);
		
		assertThat(productService.getProduct(id)).isEqualTo(productDto);
		assertThat(productService.getProduct(id)).isNotNull();
	}
	
	@Test
	public void getProductFailProductDoesNotExist() throws Exception{
		Integer id = 1;
		
		NotFoundExceptionRequest exception = assertThrows(NotFoundExceptionRequest.class,
				() -> {
					productService.getProduct(id);
				});
		assertThat(exception.getMessage()).isEqualTo("Product with id "+
				id+ " not found");
	}
	
	@Test
	public void addProductSuccess() throws Exception{
		Product product = new Product();
		product.setName("test!@#Product");
		product.setPrice(5.0);
		product.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		product.setQuantity(1);
		product.setMessage("test!@#Product");
		product.setRating(5.0);
		
		Set<Condition> conditions = new HashSet<>();
		conditions.add(new Condition("USED"));
		conditions.add(new Condition("DAMAGED"));
		
		product.setConditions(conditions);
		Category category = new Category("test!@#Product");
		product.setCategory(category);
		
		Customer customer = new Customer();
		String customerName = "test!@#łUsername";
		Integer id = 1;
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
		Set<Role> roles = new HashSet<>();
		roles.add(roleUser);
		customer.setRoles(roles);
		product.setOwner(customer);
		product.setQuantityReviews(0);
		product.setQuantityRatings(0);
		
		// when and necessary variables
		
		Set<String> conditionNames = conditions.stream().map(
				condition -> condition.getName()).collect(Collectors.toSet());
		
		when(conditionRepository.findByNameIn(conditionNames)).thenReturn(conditions);
		when(categoryRepository.findByName("test!@#Product")).thenReturn(Optional.of(category));
		when(customerRepository.findByUsername(customerName)).thenReturn(Optional.of(customer));
		
		
		Authentication authentication = Mockito.mock(Authentication.class);
		when(authentication.getName()).thenReturn(customerName);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		
		ProductDto productDto = modelMapper.map(product, ProductDto.class);
		
		ProductDto returnedProductDto = productService.addProduct(productDto);
		
		//then
		assertThat(new Date(productDto.getCreatedAt().getTime()))
			.isBeforeOrEqualTo(new Date(returnedProductDto.getCreatedAt().getTime()));
		
		productDto.setCreatedAt(null);
		returnedProductDto.setCreatedAt(null);
		
		assertThat(returnedProductDto).isEqualTo(productDto);
		assertThat(returnedProductDto).isNotNull();
	}
	
	@Test
	public void addProductFailCustomerDoesNotExist() {
		Product product = new Product();
		
		Customer customer = new Customer();
		String customerName = "test!@#łUsername";
		customer.setUsername(customerName);

		// when and necessary variables	
		
		Authentication authentication = Mockito.mock(Authentication.class);
		when(authentication.getName()).thenReturn(customerName);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		
		ProductDto productDto = modelMapper.map(product, ProductDto.class);
		
		//then 
		UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
				() -> {
					productService.addProduct(productDto);
				});
		
		assertThat(exception.getMessage()).isEqualTo("User "
				+ "not found with username: "+customerName);
	}
	
	@Test
	public void addProductFailCategoryDoesNotExist() {
		Product product = new Product();
		Category category = new Category("test!@#Product");
		product.setCategory(category);
		
		Customer customer = new Customer();
		String customerName = "test!@#łUsername";
		customer.setUsername(customerName);

		// when and necessary variables	
		when(customerRepository.findByUsername(customerName)).thenReturn(Optional.of(customer));
		
		Authentication authentication = Mockito.mock(Authentication.class);
		when(authentication.getName()).thenReturn(customerName);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		
		ProductDto productDto = modelMapper.map(product, ProductDto.class);
		
		//then 
		NotFoundExceptionRequest exception = assertThrows(NotFoundExceptionRequest.class,
				() -> {
					productService.addProduct(productDto);
				});
		
		assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(exception.getMessage()).isEqualTo("Category with name "+
				category.getName()+" does not exist");
	}
	
}

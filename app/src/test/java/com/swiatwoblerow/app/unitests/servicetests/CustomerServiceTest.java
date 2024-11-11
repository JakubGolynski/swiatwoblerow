package com.swiatwoblerow.app.unitests.servicetests;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.swiatwoblerow.app.dto.CustomerDto;
import com.swiatwoblerow.app.entity.Address;
import com.swiatwoblerow.app.entity.Category;
import com.swiatwoblerow.app.entity.Country;
import com.swiatwoblerow.app.entity.Customer;
import com.swiatwoblerow.app.entity.Role;
import com.swiatwoblerow.app.exceptions.AlreadyExistsException;
import com.swiatwoblerow.app.repository.AddressRepository;
import com.swiatwoblerow.app.repository.CountryRepository;
import com.swiatwoblerow.app.repository.CustomerRepository;
import com.swiatwoblerow.app.repository.RoleRepository;
import com.swiatwoblerow.app.service.CustomerPrincipal;
import com.swiatwoblerow.app.service.CustomerServiceImpl;
import com.swiatwoblerow.app.service.filter.CustomerFilter;
import com.swiatwoblerow.app.service.interfaces.CustomerService;
import com.swiatwoblerow.app.validators.interfaces.CustomerValidator;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

	@Mock
	private CustomerRepository customerRepository;
	@Mock
	private AddressRepository addressRepository;
	@Mock
	private CountryRepository countryRepository;
	@Mock
	private RoleRepository roleRepository;
	@Mock
	private CustomerValidator customerValidator;
	private ModelMapper modelMapper = new ModelMapper();
	private CustomerService customerService;
	
	@BeforeEach
	void setUp() {
		customerService = new CustomerServiceImpl(customerRepository,
				addressRepository, countryRepository, roleRepository,
				customerValidator, modelMapper);
	}
	
	@Test
	public void returnUserDetails() {
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
		
		Country country = new Country("Poland2");
		address.setCountry(country);
		customer.setAddress(address);
		
		Role roleUser = new Role("ROLE_USER2");

		customer.setRole(roleUser);
		
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(roleUser.getName()));
		
		UserDetails customerPrincipal = new CustomerPrincipal(customer.getUsername(),
				customer.getPassword(),authorities);
		
		Optional<Customer> optionalCustomer = Optional.of(customer);
		
		when(customerRepository.findByUsername(customerName)).thenReturn(optionalCustomer);
		
		UserDetails returnedCustomerPrincipal = customerService.loadUserByUsername(customerName);
		
		assertThat(customerPrincipal.getUsername()).isEqualTo(returnedCustomerPrincipal.getUsername());
		assertThat(customerPrincipal.getPassword()).isEqualTo(returnedCustomerPrincipal.getPassword());
		assertThat(customerPrincipal.getAuthorities()).isEqualTo(returnedCustomerPrincipal.getAuthorities());
		assertThat(customerPrincipal).isNotNull();
	}
	
	@Test
	public void getCustomersSuccess() {
		Customer customer1 = new Customer();
		String customer1Name = "test!@#łUsername2";
		customer1.setUsername(customer1Name);
		customer1.setPassword("testłPassword2");
		customer1.setFirstName("testł!@#2");
		customer1.setLastName("testł!@#2");
		customer1.setEmail("testł!@#2");
		customer1.setTelephone("+48512806005");
		
		Address address = new Address();
		address.setCity("Warsaw");
		address.setStreet("Piłsudskiego");
		address.setHouseNumber("471A");
		
		Country country = new Country("Poland2");
		address.setCountry(country);
		customer1.setAddress(address);
		
		Role roleUser = new Role("ROLE_USER2");
		
		customer1.setRole(roleUser);
		
		Customer customer2 = new Customer();
		String customer2Name = "test!@#łUsername";
		customer2.setUsername(customer2Name);
		customer2.setPassword("testłPassword");
		customer2.setFirstName("testł!@#");
		customer2.setLastName("testł!@#");
		customer2.setEmail("testł!@#");
		customer2.setTelephone("+48512806005");
		
		customer2.setAddress(address);
		customer2.setRole(roleUser);
		
		List<Customer> customers = new ArrayList<>();
		
		customers.add(customer1);
		customers.add(customer2);
		
		List<Integer> customersIds = new ArrayList<>();
		customersIds.add(customer1.getId());
		customersIds.add(customer2.getId());
		
		CustomerFilter customerFilter = new CustomerFilter();
		
		Sort sortBy = Sort.by(customerFilter.getSort());
		
		when(customerRepository.getCustomerIdList(customerFilter)).thenReturn(customersIds);
		when(customerRepository.findByIdIn(customersIds,sortBy)).thenReturn(customers);

		List<CustomerDto> returnCustomers = customers.stream().map(
				customer -> modelMapper.map(customer, CustomerDto.class))
				.collect(Collectors.toList());
		
		assertThat(returnCustomers).isEqualTo(customerService.getCustomers(customerFilter));
		assertThat(returnCustomers).isNotEmpty();
		assertThat(returnCustomers).isNotNull();
	}
	
	@Test
	public void getCustomerSuccess() {
		Customer customer = new Customer();
		String customerName = "test!@#łUsername";
		Integer id = 1;
		customer.setId(id);
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
		
		Country country = new Country("Poland");
		address.setCountry(country);
		customer.setAddress(address);
		
		Role roleUser = new Role("ROLE_USER");
		customer.setRole(roleUser);
		
		Optional<Customer> optionalCustomer = Optional.of(customer);
		
		when(customerRepository.findById(id)).thenReturn(optionalCustomer);
		
		CustomerDto customerDto = modelMapper.map(customer, CustomerDto.class);
		
		assertThat(customerDto).isEqualTo(customerService.getCustomer(id));
		assertThat(customerDto).isNotNull();
	}
	
	@Test
	public void getCustomerFailCustomerDoesNotExist() throws Exception{
		Integer id = 1;
		
		UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
				() -> {
					customerService.getCustomer(id);
				});
		assertThat(exception.getMessage()).isEqualTo("User"+
				" not found with id: "+ id);
	}
	
	@Test
	public void addCustomerSuccess() throws Exception{
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
		customer.setRole(roleUser);
		
		CustomerDto customerDto = modelMapper.map(customer, CustomerDto.class);
		
		when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(roleUser));
		when(countryRepository.findByName("Poland")).thenReturn(Optional.of(country));
		
		//In addCustomer method, these fields are purged for security reasons
		customerDto.setId(null);
		customerDto.setUsername(null);
		customerDto.setPassword(null);
		
		assertThat(customerDto).isEqualTo(customerService.addCustomer(customerDto));
		assertThat(customerDto).isNotNull();
	}
	
	@Test
	public void addCustomerFailUsernameAlreadyInUse() throws Exception{
		Customer customer = new Customer();
		String customerName = "test!@#łUsername";
		customer.setUsername(customerName);

		CustomerDto customerDto = modelMapper.map(customer, CustomerDto.class);
		
		doThrow(new AlreadyExistsException("Username "+customerDto.getUsername()+" is already in use"))
		.when(customerValidator).validate(customerDto);
		
		AlreadyExistsException exception = assertThrows(AlreadyExistsException.class,
				() -> {
					customerService.addCustomer(customerDto);
				});
		
		assertThat(exception.getStatus()).isEqualTo(HttpStatus.CONFLICT);
		assertThat(exception.getMessage()).isEqualTo("Username "+customerName+" is already in use");
	}
	
	@Test
	public void addCustomerFailEmailAlreadyInUse() throws Exception{
		Customer customer = new Customer();
		String customerName = "test!@#łUsername";
		String customerEmail = "test!@#EMail";
		customer.setUsername(customerName);
		customer.setEmail(customerEmail);

		CustomerDto customerDto = modelMapper.map(customer, CustomerDto.class);
		
		doThrow(new AlreadyExistsException("Email "+customerDto.getEmail()+" is already in use"))
			.when(customerValidator).validate(customerDto);
		
		AlreadyExistsException exception = assertThrows(AlreadyExistsException.class,
				() -> {
					customerService.addCustomer(customerDto);
				});
		
		assertThat(exception.getStatus()).isEqualTo(HttpStatus.CONFLICT);
		assertThat(exception.getMessage()).isEqualTo("Email "+customerEmail+" is already in use");
	}
}

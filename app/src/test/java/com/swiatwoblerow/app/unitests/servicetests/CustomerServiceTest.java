package com.swiatwoblerow.app.unitests.servicetests;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.swiatwoblerow.app.config.jwt.JwtUtils;
import com.swiatwoblerow.app.dto.CustomerDto;
import com.swiatwoblerow.app.entity.Address;
import com.swiatwoblerow.app.entity.Country;
import com.swiatwoblerow.app.entity.Customer;
import com.swiatwoblerow.app.entity.Role;
import com.swiatwoblerow.app.repository.AddressRepository;
import com.swiatwoblerow.app.repository.CountryRepository;
import com.swiatwoblerow.app.repository.CustomerRepository;
import com.swiatwoblerow.app.service.CustomerPrincipal;
import com.swiatwoblerow.app.service.CustomerServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
	
	@Mock
	private AuthenticationManager authenticationManager;
	
	@Mock
	private CustomerRepository customerRepository;
	
	@Mock
	private AddressRepository addressRepository;
	
	@Mock
	private CountryRepository countryRepository;
	
	@Mock
	private JwtUtils jwtUtils;
	
	@Mock
	private ModelMapper modelMapper;
	
	private CustomerServiceImpl customerService;
	
	@BeforeEach
	void setUp() {
		customerService = new CustomerServiceImpl(authenticationManager,
				customerRepository,addressRepository, countryRepository,jwtUtils, modelMapper);
	}
	
	@Test
	public void shouldReturnUserDetails() {
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
		
		Country country = new Country("Poland");
		address.setCountry(country);
		customer.setAddress(address);
		
		Role roleUser = new Role("ROLE_USER");
		
		Set<Role> roles = new HashSet<>();
		roles.add(roleUser);
		customer.setRoles(roles);
		
		List<GrantedAuthority> authorities =
				customer.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toList());
		
		UserDetails customerPrincipal = new CustomerPrincipal(customer.getUsername(),
				customer.getPassword(),authorities);
		
		Optional<Customer> optionalCustomer = Optional.of(customer);
		
		when(customerRepository.findByUsername(customerName)).thenReturn(optionalCustomer);
		
		UserDetails returnedCustomerPrincipal = customerService.loadUserByUsername(customerName);
		
		assertThat(customerPrincipal.getUsername()).isEqualTo(returnedCustomerPrincipal.getUsername());
		assertThat(customerPrincipal.getPassword()).isEqualTo(returnedCustomerPrincipal.getPassword());
		assertThat(customerPrincipal.getAuthorities()).isEqualTo(returnedCustomerPrincipal.getAuthorities());
	}
	
	@Test
	public void shouldGetCustomers() {
		Customer customer1 = new Customer();
		String customer1Name = "test!@#łUsername";
		customer1.setUsername(customer1Name);
		customer1.setPassword("testłPassword");
		customer1.setFirstName("testł!@#");
		customer1.setLastName("testł!@#");
		customer1.setEmail("testł!@#");
		customer1.setTelephone("+48512806005");
		
		Address address = new Address();
		address.setCity("Warsaw");
		address.setStreet("Piłsudskiego");
		address.setHouseNumber("471A");
		
		Country country = new Country("Poland");
		address.setCountry(country);
		customer1.setAddress(address);
		
		Role roleUser = new Role("ROLE_USER");
		
		Set<Role> roles = new HashSet<>();
		roles.add(roleUser);
		customer1.setRoles(roles);
		
		Customer customer2 = new Customer();
		String customer2Name = "test!@#łUsername";
		customer2.setUsername(customer2Name);
		customer2.setPassword("testłPassword");
		customer2.setFirstName("testł!@#");
		customer2.setLastName("testł!@#");
		customer2.setEmail("testł!@#");
		customer2.setTelephone("+48512806005");
		
		customer2.setAddress(address);
		
		customer1.setRoles(roles);
		
		List<Customer> customers = new ArrayList<>();
		
		customers.add(customer1);
		customers.add(customer2);
		
		when(customerRepository.findAll()).thenReturn(customers);

		List<CustomerDto> returnCustomers = customers.stream().map(
				customer -> modelMapper.map(customer, CustomerDto.class))
				.collect(Collectors.toList());
		
		assertThat(returnCustomers).isEqualTo(customerService.getCustomers());
	}
	
	@Test
	public void shouldGetCustomer() {
		Customer customer = new Customer();
		String customerName = "test!@#łUsername";
		customer.setId(1);
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
		Set<Role> roles = new HashSet<>();
		roles.add(roleUser);
		customer.setRoles(roles);
		
		Optional<Customer> optionalCustomer = Optional.of(customer);
		
		when(customerRepository.findById(1)).thenReturn(optionalCustomer);
		
		CustomerDto customerDto = modelMapper.map(optionalCustomer.get(), CustomerDto.class);
		
		assertThat(customerDto).isEqualTo(customerService.getCustomer(1));
	}
}

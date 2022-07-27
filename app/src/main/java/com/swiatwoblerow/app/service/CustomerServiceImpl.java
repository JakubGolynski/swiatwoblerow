package com.swiatwoblerow.app.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.swiatwoblerow.app.config.jwt.JwtUtils;
import com.swiatwoblerow.app.dto.AddressDto;
import com.swiatwoblerow.app.dto.CustomerDto;
import com.swiatwoblerow.app.entity.Address;
import com.swiatwoblerow.app.entity.Country;
import com.swiatwoblerow.app.entity.Customer;
import com.swiatwoblerow.app.entity.Role;
import com.swiatwoblerow.app.repository.CustomerRepository;
import com.swiatwoblerow.app.service.interfaces.CustomerService;

@Service("customerServiceImpl")
public class CustomerServiceImpl implements CustomerService, UserDetailsService {
	
	private AuthenticationManager authenticationManager;
	
	private CustomerRepository customerRepository;
	
	private JwtUtils jwtUtils;
	
	public CustomerServiceImpl(AuthenticationManager authenticationManager, CustomerRepository customerRepository,
			JwtUtils jwtUtils) {
		this.authenticationManager = authenticationManager;
		this.customerRepository = customerRepository;
		this.jwtUtils = jwtUtils;
	}

	@Override
	public UserDetails loadUserByUsername(String username)
											throws UsernameNotFoundException {
		Customer customer = customerRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User"+
				" not found with username: "+ username));
		
		List<GrantedAuthority> authorities =
			customer.getRoles().stream()
			.map(role -> new SimpleGrantedAuthority(role.getName()))
			.collect(Collectors.toList());
												
		return new CustomerPrincipal(customer.getUsername(),
				customer.getPassword(),authorities);
	}
	
	@Override
	public CustomerDto login(CustomerDto customerDto) throws BadCredentialsException{
		UsernamePasswordAuthenticationToken tokenAuthetication = 
				new UsernamePasswordAuthenticationToken(customerDto.getUsername(),customerDto.getPassword());
		Authentication authetication = authenticationManager.authenticate(tokenAuthetication);
		SecurityContextHolder.getContext().setAuthentication(authetication);
		return getLoggedCustomer();
	}
	
	@Override
	public CustomerDto getLoggedCustomer(){
		CustomerDto customerDto = new CustomerDto();
		CustomerPrincipal customerPrincipal = (CustomerPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Customer customer = customerRepository.findByUsername(customerPrincipal.getUsername()).orElseThrow(
				() -> new UsernameNotFoundException("User not found with username "+ customerPrincipal.getUsername()));
		customerDto.setUsername(customer.getUsername());
		customerDto.setFirstName(customer.getFirstName());
		customerDto.setLastName(customer.getLastName());
		customerDto.setEmail(customer.getEmail());
		Address address = customer.getAddress();
		customerDto.setCustomerAddress(new AddressDto(address.getId(),
				address.getCity(),address.getStreet(),address.getHouseNumber(),
				address.getCountry().getName()));
		customerDto.setJwtToken(jwtUtils.generateJwtToken(customerPrincipal.getUsername()));
		List<String> roles = customerPrincipal.getAuthorities()
				.stream().map(role -> role.getAuthority()).collect(Collectors.toList());
		customerDto.setRoles(roles);
		return customerDto; 
	}
	
	@Override 
	public List<CustomerDto> getCustomers(){
		
		return customerRepository.findAll().stream().map(
				customer -> new CustomerDto(
						customer.getUsername(),null,
						customer.getFirstName(),customer.getLastName(),
						customer.getEmail(),customer.getTelephone(),null,
						new AddressDto(customer.getAddress().getId(),
								customer.getAddress().getCity(),customer.getAddress().getStreet(),
								customer.getAddress().getHouseNumber(),customer.getAddress().getCountry().getName()),
						customer.getRoles()
						.stream().map(role -> role.getName()).collect(Collectors.toList())))
				.collect(Collectors.toList());
	}

	@Override
	public CustomerDto getCustomer(Integer id) throws UsernameNotFoundException{
		Customer customer = customerRepository.findById(id).orElseThrow(
				() -> new UsernameNotFoundException("User"+
						" not found with id: "+ id));
		CustomerDto customerDto = new CustomerDto();
		customerDto.setUsername(customer.getUsername());
		customerDto.setFirstName(customer.getFirstName());
		customerDto.setLastName(customer.getLastName());
		customerDto.setEmail(customer.getEmail());
		Address address = customer.getAddress();
		customerDto.setCustomerAddress(new AddressDto(address.getId(),
				address.getCity(),address.getStreet(),address.getHouseNumber(),
				address.getCountry().getName()));
		customerDto.setRoles(customer.getRoles().stream().map(
				role -> role.getName()).collect(Collectors.toList()));
		return customerDto; 
	}

	@Override
	public CustomerDto addCustomer(CustomerDto customerDto) {
		Set<Role> roles = new HashSet<>();
		roles.add(new Role("ROLE_USER"));
		Address address = new Address();
		address.setId(customerDto.getCustomerAddress().getId());
		address.setCity(customerDto.getCustomerAddress().getCity());
		address.setStreet(customerDto.getCustomerAddress().getStreet());
		address.setHouseNumber(customerDto.getCustomerAddress().getHouseNumber());
		address.setCountry(new Country(customerDto.getCustomerAddress().getHouseNumber()));
		Customer customer = new Customer(
				customerDto.getUsername(),customerDto.getPassword(),
				customerDto.getFirstName(),customerDto.getLastName(),
				customerDto.getEmail(),customerDto.getTelephone(),
				address,roles
				);
		customerRepository.save(customer);
		return customerDto;
	}
	
}

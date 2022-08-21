package com.swiatwoblerow.app.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
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
import com.swiatwoblerow.app.dto.RoleDto;
import com.swiatwoblerow.app.entity.Address;
import com.swiatwoblerow.app.entity.Country;
import com.swiatwoblerow.app.entity.Customer;
import com.swiatwoblerow.app.entity.Role;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;
import com.swiatwoblerow.app.repository.AddressRepository;
import com.swiatwoblerow.app.repository.CountryRepository;
import com.swiatwoblerow.app.repository.CustomerRepository;
import com.swiatwoblerow.app.service.interfaces.CustomerService;

@Service("customerServiceImpl")
public class CustomerServiceImpl implements CustomerService, UserDetailsService {
	
	private AuthenticationManager authenticationManager;
	
	private CustomerRepository customerRepository;
	
	private AddressRepository addressRepository;
	
	private CountryRepository countryRepository;
	
	private JwtUtils jwtUtils;
	
	private ModelMapper modelMapper;

	public CustomerServiceImpl(AuthenticationManager authenticationManager, CustomerRepository customerRepository,
			AddressRepository addressRepository, CountryRepository countryRepository, JwtUtils jwtUtils,
			ModelMapper modelMapper) {
		this.authenticationManager = authenticationManager;
		this.customerRepository = customerRepository;
		this.addressRepository = addressRepository;
		this.countryRepository = countryRepository;
		this.jwtUtils = jwtUtils;
		this.modelMapper = modelMapper;
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
	public CustomerDto login(CustomerDto customerDto) throws BadCredentialsException,UsernameNotFoundException{
		UsernamePasswordAuthenticationToken tokenAuthetication = 
				new UsernamePasswordAuthenticationToken(customerDto.getUsername(),customerDto.getPassword());
		Authentication authetication = authenticationManager.authenticate(tokenAuthetication);
		SecurityContextHolder.getContext().setAuthentication(authetication);
		
		CustomerPrincipal customerPrincipal = (CustomerPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Customer customer = customerRepository.findByUsername(customerPrincipal.getUsername()).orElseThrow(
				() -> new UsernameNotFoundException("User not found with username "+ customerPrincipal.getUsername()));
		CustomerDto returnCustomerDto = modelMapper.map(customer, CustomerDto.class);
		returnCustomerDto.setJwtToken(jwtUtils.generateJwtToken(customerPrincipal.getUsername()));
		return returnCustomerDto;
	}
	
	@Override 
	public List<CustomerDto> getCustomers(){
		
		return customerRepository.findAll().stream().map(
				customer -> modelMapper.map(customer, CustomerDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public CustomerDto getCustomer(Integer id) throws UsernameNotFoundException{
		Customer customer = customerRepository.findById(id).orElseThrow(
				() -> new UsernameNotFoundException("User"+
						" not found with id: "+ id));
		CustomerDto customerDto = modelMapper.map(customer, CustomerDto.class);
		return customerDto; 
	}

	@Override
	public CustomerDto addCustomer(CustomerDto customerDto) throws NotFoundExceptionRequest{
		Set<Role> roles = new HashSet<>();
		roles.add(new Role("ROLE_USER"));
		Address address = new Address();
		address.setId(customerDto.getAddress().getId());
		address.setCity(customerDto.getAddress().getCity());
		address.setStreet(customerDto.getAddress().getStreet());
		address.setHouseNumber(customerDto.getAddress().getHouseNumber());
		Country country = countryRepository.findByName(customerDto.getAddress().getCountry().getName())
				.orElseThrow(() -> new NotFoundExceptionRequest("Country "+
						"with name "+customerDto.getAddress().getCountry().getName() +" does not exist"));
		
		address.setCountry(country);
		addressRepository.save(address);
		Customer customer = new Customer(
				customerDto.getUsername(),customerDto.getPassword(),
				customerDto.getFirstName(),customerDto.getLastName(),
				customerDto.getEmail(),customerDto.getTelephone(),
				address,roles
				);
		customerRepository.save(customer);
		CustomerDto returnCustomerDto = new CustomerDto();
		
		returnCustomerDto.setUsername(customerDto.getUsername());
		returnCustomerDto.setFirstName(customerDto.getFirstName());
		returnCustomerDto.setLastName(customerDto.getLastName());
		returnCustomerDto.setEmail(customerDto.getEmail());
		returnCustomerDto.setTelephone(customerDto.getTelephone());
		AddressDto addressDto = modelMapper.map(address, AddressDto.class);
		returnCustomerDto.setAddress(addressDto);
		Set<RoleDto> rolesDto = roles.stream().map(
				role -> modelMapper.map(role, RoleDto.class)).collect(Collectors.toSet());
		returnCustomerDto.setRoles(rolesDto);
		
		return returnCustomerDto;
	}
	
}

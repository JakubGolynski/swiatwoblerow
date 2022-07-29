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
import com.swiatwoblerow.app.service.interfaces.MappingConverter;

@Service("customerServiceImpl")
public class CustomerServiceImpl implements CustomerService, UserDetailsService {
	
	private AuthenticationManager authenticationManager;
	
	private CustomerRepository customerRepository;
	
	private JwtUtils jwtUtils;
	
	private MappingConverter mappingConverter;
	
	public CustomerServiceImpl(AuthenticationManager authenticationManager, CustomerRepository customerRepository,
			JwtUtils jwtUtils, MappingConverter mappingConverter) {
		this.authenticationManager = authenticationManager;
		this.customerRepository = customerRepository;
		this.jwtUtils = jwtUtils;
		this.mappingConverter = mappingConverter;
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
		CustomerDto returnCustomerDto = new CustomerDto();
		returnCustomerDto.setUsername(customer.getUsername());
		returnCustomerDto.setFirstName(customer.getFirstName());
		returnCustomerDto.setLastName(customer.getLastName());
		returnCustomerDto.setEmail(customer.getEmail());
		returnCustomerDto.setTelephone(customer.getTelephone());
		returnCustomerDto.setJwtToken(jwtUtils.generateJwtToken(customerPrincipal.getUsername()));
		returnCustomerDto.setCustomerAddress(mappingConverter.convertAddressToAddressDto(customer.getAddress()));
		returnCustomerDto.setRoles(mappingConverter.convertRolesToTheirNames(customer.getRoles()));
		return returnCustomerDto;
	}
	
	@Override
	public CustomerDto getLoggedCustomer() throws UsernameNotFoundException{
		CustomerDto customerDto = new CustomerDto();
		CustomerPrincipal customerPrincipal = (CustomerPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Customer customer = customerRepository.findByUsername(customerPrincipal.getUsername()).orElseThrow(
				() -> new UsernameNotFoundException("User not found with username "+ customerPrincipal.getUsername()));
		customerDto.setUsername(customer.getUsername());
		customerDto.setFirstName(customer.getFirstName());
		customerDto.setLastName(customer.getLastName());
		customerDto.setEmail(customer.getEmail());
		customerDto.setCustomerAddress(mappingConverter.convertAddressToAddressDto(customer.getAddress()));
		customerDto.setRoles(mappingConverter.convertRolesToTheirNames(customer.getRoles()));
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
						mappingConverter.convertRolesToTheirNames(customer.getRoles())))
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
		customerDto.setCustomerAddress(mappingConverter.convertAddressToAddressDto(customer.getAddress()));
		customerDto.setRoles(mappingConverter.convertRolesToTheirNames(customer.getRoles()));
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

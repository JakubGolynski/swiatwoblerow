package com.swiatwoblerow.app.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.swiatwoblerow.app.dto.CustomerDto;
import com.swiatwoblerow.app.entity.Address;
import com.swiatwoblerow.app.entity.Category;
import com.swiatwoblerow.app.entity.Country;
import com.swiatwoblerow.app.entity.Customer;
import com.swiatwoblerow.app.entity.Role;
import com.swiatwoblerow.app.exceptions.AlreadyExistsException;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;
import com.swiatwoblerow.app.repository.AddressRepository;
import com.swiatwoblerow.app.repository.CountryRepository;
import com.swiatwoblerow.app.repository.CustomerRepository;
import com.swiatwoblerow.app.repository.RoleRepository;
import com.swiatwoblerow.app.service.filter.CustomerFilter;
import com.swiatwoblerow.app.service.interfaces.CustomerService;
import com.swiatwoblerow.app.validators.interfaces.CustomerValidator;

@Service
public class CustomerServiceImpl implements CustomerService, UserDetailsService {
	
	private CustomerRepository customerRepository;
	private AddressRepository addressRepository;
	private CountryRepository countryRepository;
	private RoleRepository roleRepository;
	private CustomerValidator customerValidator;
	private ModelMapper modelMapper;
	
	public CustomerServiceImpl(CustomerRepository customerRepository, AddressRepository addressRepository,
			CountryRepository countryRepository, RoleRepository roleRepository, CustomerValidator customerValidator,
			ModelMapper modelMapper) {
		this.customerRepository = customerRepository;
		this.addressRepository = addressRepository;
		this.countryRepository = countryRepository;
		this.roleRepository = roleRepository;
		this.customerValidator = customerValidator;
		this.modelMapper = modelMapper;
	}

	@Override
	public UserDetails loadUserByUsername(String username)
											throws UsernameNotFoundException {
		Customer customer = customerRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User"+
				" not found with username: "+ username));
		
		List<GrantedAuthority> authorities = new ArrayList<>();
		if(customer.getRole()!=null) {
			Role role = customer.getRole();
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}
												
		return new CustomerPrincipal(customer.getUsername(),
				customer.getPassword(),authorities);
	}
	
	@Override 
	public List<CustomerDto> getCustomers(CustomerFilter customerFilter){
		
		Sort sortBy = Sort.by(Sort.Direction.ASC,customerFilter.getSort());
		
		return customerRepository.findByIdIn(
				customerRepository.getCustomerIdList(customerFilter),sortBy)
				.stream().map(
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
	public CustomerDto addCustomer(CustomerDto customerDto) 
			throws NotFoundExceptionRequest, AlreadyExistsException{
		customerValidator.validate(customerDto);
		
		Role role = roleRepository.findByName("ROLE_USER").orElseThrow(
				() -> new NotFoundExceptionRequest("ROLE_USER does not exist in database"));
		
		Address address = modelMapper.map(customerDto.getAddress(), Address.class);
		
		Country country = countryRepository.findByName(customerDto.getAddress().getCountry().getName())
				.orElseThrow(() -> new NotFoundExceptionRequest("Country "+
						"with name "+customerDto.getAddress().getCountry().getName() +" does not exist"));
		address.setCountry(country);
		addressRepository.save(address);
		Customer customer = new Customer(
				customerDto.getUsername(),customerDto.getPassword(),
				customerDto.getFirstName(),customerDto.getLastName(),
				customerDto.getEmail(),customerDto.getTelephone(),
				address,role);
		
		customerRepository.save(customer);
		CustomerDto returnCustomerDto = modelMapper.map(customer, CustomerDto.class);	
		return returnCustomerDto;
	}
	
}

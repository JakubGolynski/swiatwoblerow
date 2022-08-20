package com.swiatwoblerow.app.unitests.repositorytests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.swiatwoblerow.app.entity.Address;
import com.swiatwoblerow.app.entity.Country;
import com.swiatwoblerow.app.entity.Customer;
import com.swiatwoblerow.app.entity.Role;
import com.swiatwoblerow.app.repository.AddressRepository;
import com.swiatwoblerow.app.repository.CountryRepository;
import com.swiatwoblerow.app.repository.CustomerRepository;
import com.swiatwoblerow.app.repository.RoleRepository;

@DataJpaTest
public class CustomerRepositoryTest {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Test
	public void customerShouldBeNullIfRepositoryIsEmpty() {
		String customerName = "testł!@#Username";
		Customer customer = customerRepository.findByUsername(customerName).orElse(null);
		assertThat(customer).isNull();
	}
	
	@Test
	public void shouldThrowExceptionIfRepositoryIsEmpty() throws UsernameNotFoundException{
		String customerName = "testł!@#Username";
		assertThatThrownBy(() -> {
			Customer customer = customerRepository.findByUsername(customerName)
					.orElseThrow(() -> new UsernameNotFoundException("User"+
					" not found with username: "+ customerName));
        })
		.isInstanceOf(UsernameNotFoundException.class)
        .hasMessage("User"+
				" not found with username: "+ customerName);
	}
	
	@Test
	public void shouldFindCustomerIfRepositoryIsNotEmpty() {
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
		
		customerRepository.save(customer);
		Customer expectedCustomer = customerRepository.findByUsername(customerName).orElse(null);
		
		assertThat(customer).isEqualTo(expectedCustomer);
	}
}
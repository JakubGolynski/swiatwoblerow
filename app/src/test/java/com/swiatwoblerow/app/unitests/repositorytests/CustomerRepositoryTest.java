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
import com.swiatwoblerow.app.repository.CustomerRepository;

@DataJpaTest
public class CustomerRepositoryTest {
	
	@Autowired
	private CustomerRepository customerRepository;
	
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
		String customerName = "test!@#łUsername";
		Customer customer = new Customer();
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
		Set<Role> roles = new HashSet<>();
		roles.add(new Role("ROLE_USER"));
		customer.setRoles(roles);
		
		customerRepository.save(customer);
		Customer expectedCustomer = customerRepository.findByUsername(customerName).orElse(null);
		
		assertThat(customer).isEqualTo(expectedCustomer);
	}
}

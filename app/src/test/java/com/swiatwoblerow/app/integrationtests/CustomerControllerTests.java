package com.swiatwoblerow.app.integrationtests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiatwoblerow.app.dto.CustomerDto;
import com.swiatwoblerow.app.entity.Address;
import com.swiatwoblerow.app.entity.Category;
import com.swiatwoblerow.app.entity.Country;
import com.swiatwoblerow.app.entity.Customer;
import com.swiatwoblerow.app.entity.Role;
import com.swiatwoblerow.app.repository.AddressRepository;
import com.swiatwoblerow.app.repository.CountryRepository;
import com.swiatwoblerow.app.repository.CustomerRepository;
import com.swiatwoblerow.app.repository.RoleRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTests {
	
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Test
	public void shouldGetUserSuccess() throws Exception {
		
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
		
		Country country = new Country("Test");
		countryRepository.save(country);
		address.setCountry(country);
		customer.setAddress(address);
		
		Role roleUser = new Role("ROLE_BAKER");
		roleRepository.save(roleUser);
		
		customer.setRole(roleUser);
		
		Set<Category> managedCategories = new HashSet<>();
		customerRepository.save(customer);
		
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/customers/"+customer.getId())
				.header("Authorization", "Bearer " + getJwt("goly", "goly"))
//				.characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		
		CustomerDto returnedCustomerDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), CustomerDto.class);
		
		CustomerDto customerDto = modelMapper.map(customer, CustomerDto.class);
		
//		assertThat(customerDto).isEqualTo(returnedCustomerDto);
		assertThat(returnedCustomerDto.getJwtToken()).isNull();
		assertThat(returnedCustomerDto.getAddress().getCountry()).isNotNull();
		assertThat(returnedCustomerDto.getPassword()).isNull();
		assertThat(returnedCustomerDto.getUsername()).isEqualTo(customer.getUsername());
		assertThat(returnedCustomerDto.getRole()).isNotNull();
		assertThat(returnedCustomerDto.getEmail()).isNotNull();
		assertThat(returnedCustomerDto.getFirstName()).isNotNull();
		assertThat(returnedCustomerDto.getTelephone()).isNotNull();
		assertThat(returnedCustomerDto.getLastName()).isNotNull();
	}
	
	@Test
	public void shouldGetUserUnauthenticated() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/customers/1")
				.characterEncoding("utf-8")
				.header("Authorization", "Bearer "
						+"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtb2RlcmF0b3IiL"
						+ "CJpYXQiOjE2NjU0Mjc4MTQsImV4cCI6MTY2NTQyODcxNH0.WkFDmM"
						+ "kARPInNpV7VXRKScmMDsMXYeE3Suzzi9rPngLmsIexInLMQXOweMHZ6hiWnwj7pEDaMT7zJfaoKWur0w")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isUnauthorized());
	}
	
	public String getJwt(String username, String password) throws Exception{
		CustomerDto customerDto = new CustomerDto();
		customerDto.setUsername(username);
		customerDto.setPassword(password);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/login")
				.characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(customerDto)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		
		CustomerDto returnedCustomerDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), CustomerDto.class);
		
		assertThat(returnedCustomerDto.getJwtToken()).isNotNull();
		assertThat(returnedCustomerDto.getUsername()).isEqualTo(customerDto.getUsername());
		assertThat(returnedCustomerDto.getPassword()).isNull();
		
		return returnedCustomerDto.getJwtToken();
	}
	
}

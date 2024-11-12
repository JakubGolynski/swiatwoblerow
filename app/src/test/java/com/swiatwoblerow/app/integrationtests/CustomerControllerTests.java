package com.swiatwoblerow.app.integrationtests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import java.util.HashSet;
import java.util.Set;

import com.swiatwoblerow.app.config.jwt.JWTAuthorizationFilter;
import com.swiatwoblerow.app.service.interfaces.CustomerService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
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
	@Transactional
	public void shouldGetUserSuccess() throws Exception {

		Customer customerGoly = customerRepository.findByUsername("goly").orElse(null);
		
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/customers/"+customerGoly.getId())
				.header("Authorization", "Bearer " + getJwt("goly", "goly"))
//				.characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		
		CustomerDto returnedCustomerDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), CustomerDto.class);


		assertThat(returnedCustomerDto.getJwtToken()).isNull();
		assertThat(returnedCustomerDto.getAddress().getCountry()).isNotNull();
		assertThat(returnedCustomerDto.getPassword()).isNull();
		assertThat(returnedCustomerDto.getUsername()).isEqualTo(customerGoly.getUsername());
		assertThat(returnedCustomerDto.getRole()).isNotNull();
		assertThat(returnedCustomerDto.getEmail()).isNotNull();
		assertThat(returnedCustomerDto.getFirstName()).isNotNull();
		assertThat(returnedCustomerDto.getTelephone()).isNotNull();
		assertThat(returnedCustomerDto.getLastName()).isNotNull();
	}
	
	@Test
	@Transactional
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

package com.swiatwoblerow.app.integrationtests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiatwoblerow.app.dto.CustomerDto;
import com.swiatwoblerow.app.exceptions.dto.BadCredentialsExceptionDto;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTests {
	
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	public void shouldGetUserSuccess() throws Exception {
		
		String jwt = shouldGetJwt("goly","goly");
		
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/customers/1")
				.characterEncoding("utf-8")
				.header("Authorization", "Bearer "+ jwt)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		
		CustomerDto returnedCustomerDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), CustomerDto.class);
		
		assertThat(returnedCustomerDto.getJwtToken()).isNull();
		assertThat(returnedCustomerDto.getPassword()).isNull();
		assertThat(returnedCustomerDto.getUsername()).isEqualTo("goly");
		assertThat(returnedCustomerDto.getRoles()).isNotNull();
		assertThat(returnedCustomerDto.getEmail()).isNotNull();
		assertThat(returnedCustomerDto.getFirstName()).isNotNull();
		assertThat(returnedCustomerDto.getTelephone()).isNotNull();
		assertThat(returnedCustomerDto.getLastName()).isNotNull();
	}
	
//	@Test
//	public void shouldGetUserUnauthenticated() throws Exception {
//		
//		String jwt = shouldGetJwt("doesnotexist","doesnotexist");
//		
//		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/customers/1")
//				.characterEncoding("utf-8")
//				.header("Authorization", "Bearer "+ jwt)
//				.accept(MediaType.APPLICATION_JSON)
//				.contentType(MediaType.APPLICATION_JSON_VALUE))
//				.andExpect(status().isUnauthorized())
//				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
//				.andReturn();
//		
//		BadCredentialsExceptionDto exception = objectMapper.readValue(
//				mvcResult.getResponse().getContentAsString(),BadCredentialsExceptionDto.class);
//		
//		assertThat(exception).isInstanceOf(BadCredentialsExceptionDto.class);
//		assertThat(exception.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
//	}
	
	public String shouldGetJwt(String username, String password) throws Exception{
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

package com.swiatwoblerow.app.integrationtests;


import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiatwoblerow.app.dto.CategoryDto;
import com.swiatwoblerow.app.dto.CustomerDto;
import com.swiatwoblerow.app.exceptions.dto.BadCredentialsExceptionDto;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;


@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTests {
	
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	public void afterSucessfullLoginCheckTokenOnSomeEndpoint() throws Exception{
		CustomerDto customerDto = new CustomerDto();
		customerDto.setUsername("goly");
		customerDto.setPassword("goly");
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
		
		MvcResult mvcResult2 = mvc.perform(MockMvcRequestBuilders.get("/categories")
				.characterEncoding("utf-8")
				.header("Authorization", "Bearer "+returnedCustomerDto.getJwtToken())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(customerDto)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		
		List<CategoryDto> categories = objectMapper.readValue(
				mvcResult2.getResponse().getContentAsString(), new TypeReference<List<CategoryDto>>(){});
		
		assertThat(categories).isNotEmpty();
	}
	
	@Test
	public void afterUnsucessfullLoginReturnUnauthorized() throws Exception{
		CustomerDto customerDto = new CustomerDto();
		customerDto.setUsername("NotInDb");
		customerDto.setPassword("NotInDb");
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/login")
				.characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(customerDto)))
				.andExpect(status().isUnauthorized())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		
		BadCredentialsExceptionDto exception = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(),BadCredentialsExceptionDto.class);
		
		assertThat(exception).isInstanceOf(BadCredentialsExceptionDto.class);
		assertThat(exception.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}
}

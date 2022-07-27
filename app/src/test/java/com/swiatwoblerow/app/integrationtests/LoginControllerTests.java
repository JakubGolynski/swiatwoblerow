package com.swiatwoblerow.app.integrationtests;

import java.util.Base64;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTests {
	
//	@Autowired
//	private MockMvc mvc;
//	
//	@Test
//	public void afterSucessfullLoginReturnToken() throws Exception{
//		mvc.perform(MockMvcRequestBuilders.post("/login")
//				.characterEncoding("utf-8")
//				.header(HttpHeaders.AUTHORIZATION, "Basic "+ new String(Base64.getEncoder().encodeToString("giga:giga".getBytes()))))
//				.andExpect(status().isOk())
//				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
//				.andExpect(jsonPath("$.tokenType").value("Bearer"))
//				.andExpect(jsonPath("$.password").doesNotExist())
//				.andExpect(jsonPath("$.accessToken").exists())
//				.andDo(print());
//	}
//	
//	@Test
//	public void afterUnsucessfullLoginReturnUnauthorized() throws Exception{
//		mvc.perform(MockMvcRequestBuilders.post("/login")
//				.characterEncoding("utf-8")
//				.contentType(MediaType.APPLICATION_JSON)
//				.header(HttpHeaders.AUTHORIZATION, "Basic "+ new String(Base64.getEncoder().encodeToString("dontexist:pass".getBytes()))))
//				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isUnauthorized());
//	}
}

package com.swiatwoblerow.app.unitests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.*;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;



import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiatwoblerow.app.config.jwt.JwtAuthenticationEntryPoint;
import com.swiatwoblerow.app.config.jwt.JwtUtils;
import com.swiatwoblerow.app.dto.ProductDetailsDto;
import com.swiatwoblerow.app.dto.ProductDto;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;
import com.swiatwoblerow.app.service.CustomerServiceImpl;
import com.swiatwoblerow.app.service.OfferServiceImpl;

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
public class ControllerTests {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private JwtUtils jwtUtils;
	
	@MockBean
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@MockBean
	private OfferServiceImpl offerService;
	
	@MockBean
	@Qualifier("customerServiceImpl")
	private CustomerServiceImpl customerService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	public void shouldReturnProductDetailsDtoWithGivenId() throws Exception{
		ProductDetailsDto productDetailsDto = new ProductDetailsDto();
		productDetailsDto.setName("nameName");
		productDetailsDto.setPrice(219.12);
		productDetailsDto.setQuantity(1);
		productDetailsDto.setUsername("nameUsername");
		productDetailsDto.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		productDetailsDto.setLocalization("nameLocalization");
		
		given(offerService.findById(1)).willReturn(productDetailsDto);
		
		mvc.perform(MockMvcRequestBuilders.get("/api/offer/?label=1"))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(productDetailsDto)))
			.andDo(print());
	}
	
	@Test
	public void shouldProductDetailsDtoThrowNotFoundException() throws Exception{
		NotFoundExceptionRequest exception = new NotFoundExceptionRequest("Product with label "+
						" not found");
		given(offerService.findById(1)).willThrow(exception);
		
		mvc.perform(MockMvcRequestBuilders.get("/api/offer/?label=1"))
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value(exception.getMessage()));
			
	}
	
	@Test
	public void shouldReturnAllProductsFilteredByParams() throws Exception{
		
		ProductDto product1 = new ProductDto();
		product1.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		product1.setLabel(1);
		product1.setLocalization("Warszawa");
		product1.setName("nameName");
		product1.setPrice(21.11);
		
		ProductDto product2 = new ProductDto();
		product2.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		product2.setLabel(2);
		product2.setLocalization("Warszawa");
		product2.setName("nameName2");
		product2.setPrice(11.11);
		List<ProductDto> products = new ArrayList<>();
		products.add(product1);
		products.add(product2);
		Map<String,String> params = new TreeMap<>();
		params.put("localization", "Warszawa");
		params.put("name", "name");
		given(offerService.findAll(params)).willReturn(products);
		
		mvc.perform(MockMvcRequestBuilders.get("/api/category/cars/?"
				+ "localization=Warszawa&name=name")
			.characterEncoding("utf-8"))
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(products)));
	}
	
	@Test
	public void shouldAddProdcutDetailsDto() throws Exception{
		ProductDetailsDto productDetailsDto = new ProductDetailsDto();
		
		productDetailsDto.setLocalization("Wwa");
		productDetailsDto.setMessage("Sprzedam samochód");
		productDetailsDto.setName("Opel Astra 4");
		productDetailsDto.setPrice(20000.22);
		productDetailsDto.setQuantity(1);
		
		ProductDetailsDto getProductDetailsDto = new ProductDetailsDto();
		getProductDetailsDto.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		getProductDetailsDto.setLocalization("Wwa");
		getProductDetailsDto.setMessage("Sprzedam samochód");
		getProductDetailsDto.setName("Opel Astra 4");
		getProductDetailsDto.setPrice(20000.22);
		getProductDetailsDto.setQuantity(1);
		getProductDetailsDto.setUsername("username1");
		
		given(offerService.save(productDetailsDto)).willReturn(getProductDetailsDto);
		
		mvc.perform(MockMvcRequestBuilders.post("/api/offer/add")
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("utf-8")
				.content(objectMapper.writeValueAsString(productDetailsDto)))
				.andExpect(status().isOk())
				.andDo(print());
	}
	
}